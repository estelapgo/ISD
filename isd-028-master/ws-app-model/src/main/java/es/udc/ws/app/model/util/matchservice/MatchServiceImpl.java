package es.udc.ws.app.model.util.matchservice;

import es.udc.ws.app.model.util.Match.Match;
import es.udc.ws.app.model.util.Match.SqlMatchDao;
import es.udc.ws.app.model.util.Match.SqlMatchDaoFactory;
import es.udc.ws.app.model.util.Purchase.Purchase;
import es.udc.ws.app.model.util.Purchase.SqlPurchaseDao;
import es.udc.ws.app.model.util.Purchase.SqlPurchaseDaoFactory;
import es.udc.ws.app.model.util.matchservice.exceptions.*;
import es.udc.ws.util.exceptions.*;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static es.udc.ws.app.model.util.ModelConstants.MAX_PRICE;

public class MatchServiceImpl implements MatchService {

    private final DataSource dataSource;
    private SqlMatchDao matchDao = null;

    private SqlPurchaseDao purchaseDao = null;


    public MatchServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        matchDao = SqlMatchDaoFactory.getDao();
        purchaseDao = SqlPurchaseDaoFactory.getDao();
    }

    public void validateMatch(Match match) throws InputValidationException{
        PropertyValidator.validateDouble("ticketsPrice", match.getTicketsPrice(), 0, MAX_PRICE);
        PropertyValidator.validateLong("capacity", match.getCapacity(), 0, Integer.MAX_VALUE);
        PropertyValidator.validateLong("soldUnits", match.getSoldUnits(), 0, match.getCapacity());
        PropertyValidator.validateMandatoryString("visitingTeam", match.getVisitingTeam());
    }

    @Override
    public Match addMatch(Match match) throws InputValidationException{

        validateMatch(match);
        if(LocalDateTime.now().isAfter(match.getMatchDate())){
            throw new InputValidationException("Match date must be after current date");
        }
        match.setRegisterDate(LocalDateTime.now().withNano(0));

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Match createdMatch = matchDao.create(connection, match);

                connection.commit();

                return createdMatch;

            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Match findMatch(Long matchID) throws InstanceNotFoundException{
        try (Connection connection = dataSource.getConnection()){
            return matchDao.find(connection, matchID);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Match> findMatches(LocalDateTime initDate, LocalDateTime endDate) throws InputValidationException {
        List<Match> matches;

        if(initDate == null || endDate == null || endDate.isBefore(initDate)) {
            throw new InputValidationException("Formato de fechas no válido");
        }

        try(Connection connection = dataSource.getConnection()) {
            matches = matchDao.findMatches(connection,initDate,endDate);
            return matches;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Purchase buyTickets(String userEmail, String creditCardNumber, int numTickets, Long matchID)
            throws InputValidationException, InstanceNotFoundException, MatchNotAvailableException, NotEnoughTicketsException {

        PropertyValidator.validateCreditCard((creditCardNumber));

        if (userEmail == null || userEmail.length() == 0 || !validateEmail(userEmail)) {
            throw new InputValidationException("Invalid email format or email cannot be null/empty");
        }


        try (Connection connection = dataSource.getConnection()) {

            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Match match = matchDao.find(connection, matchID);
                if (LocalDateTime.now().isAfter((match.getMatchDate()))) {
                    throw new MatchNotAvailableException(LocalDateTime.now().withNano(0));

                }
                if (numTickets + match.getSoldUnits() > match.getCapacity()){
                    throw new NotEnoughTicketsException(match.getMatchID(), numTickets, match.getCapacity()-match.getSoldUnits());
                }

                Purchase purchase = purchaseDao.create(connection, new Purchase(userEmail, creditCardNumber,  LocalDateTime.now().withNano(0),
                        matchID, numTickets));



                match.setSoldUnits(match.getSoldUnits()+purchase.getUnits());
                matchDao.update(connection, match);

                connection.commit();

                return purchase;

            } catch (InstanceNotFoundException | MatchNotAvailableException | NotEnoughTicketsException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Purchase> findUserPurchases(String userEmail) throws InputValidationException {
        try(Connection connection = dataSource.getConnection()) {
             return purchaseDao.findByUser(connection,userEmail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void collectedTicket(Long purchaseID, String bankCard) throws InputValidationException, InstanceNotFoundException, AlreadyCollectedException, IncorrectBankCardException {
        PropertyValidator.validateCreditCard(bankCard);
        PropertyValidator.validateLong("purchaseID", purchaseID, 0, 99999999);

        try(Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Purchase purchase = purchaseDao.find(connection, purchaseID);

                if (purchase.getCollected()) {
                    throw new AlreadyCollectedException(purchaseID);
                }
                if (!purchase.getBankCard().equals(bankCard)) {
                    throw new IncorrectBankCardException(purchaseID);
                }

                purchase.setCollected(true);
                purchaseDao.update(connection, purchase);

                connection.commit();

            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean validateEmail(String userEmail) {
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
            return userEmail.matches(emailRegex);
    }
}