package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.util.Match.SqlMatchDao;
import es.udc.ws.app.model.util.Match.SqlMatchDaoFactory;
import es.udc.ws.app.model.util.Purchase.Purchase;
import es.udc.ws.app.model.util.Purchase.SqlPurchaseDao;
import es.udc.ws.app.model.util.Purchase.SqlPurchaseDaoFactory;
import es.udc.ws.app.model.util.matchservice.MatchService;
import es.udc.ws.app.model.util.matchservice.exceptions.*;

import es.udc.ws.util.exceptions.*;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import es.udc.ws.app.model.util.Match.Match;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import es.udc.ws.app.model.util.matchservice.MatchServiceFactory;

public class AppServiceTest {

    private static MatchService matchService = null;
    private static SqlMatchDao matchDao = null;
    private static SqlPurchaseDao purchaseDao = null;

    private final DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";

    private final String INVALID_CREDIT_CARD_NUMBER = "";

    private final long NON_EXISTENT_MATCH_ID = -1;

    private final String USER_EMAIL = "wsuser@gmail.com";

    private final long NON_EXISTENT_PURCHASE_ID = -1;


    @BeforeAll
    public static void init() {
        DataSource dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);
        matchService = MatchServiceFactory.getService();
        matchDao = SqlMatchDaoFactory.getDao();
        purchaseDao = SqlPurchaseDaoFactory.getDao();
    }

    public Match getValidMatch() {
        LocalDateTime time = LocalDateTime.now().plusDays(30).withNano(0);
        return new Match(time, 250, 60000, "Real Madrid");
    }

    private Match createMatch(Match match) {
        Match addedMatch = null;
        try {
            addedMatch = matchService.addMatch(match);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedMatch;
    }

    public void remove(Long matchID) throws InstanceNotFoundException{

        try (Connection connection = dataSource.getConnection()) {

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                matchDao.remove(connection, matchID);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeMatch(Long matchId){
        try{
            remove(matchId);
        } catch (InstanceNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    private void updateMatch(Match match) {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                matchDao.update(connection, match);

                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
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

    public Purchase findPurchase(Long purchaseId) throws InstanceNotFoundException {

        try(Connection connection = dataSource.getConnection()){

            Purchase purchase = purchaseDao.find(connection,purchaseId);
            return purchase;

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void removePurchase(Long purchaseID) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                purchaseDao.remove(connection, purchaseID);

                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
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


    @Test
    public void testAddMatchAndFindMatch() throws InstanceNotFoundException, InputValidationException {
        Match match =  getValidMatch();
        Match addedMatch = null;

        try{
            addedMatch = matchService.addMatch(match);

            Match foundMatch = matchService.findMatch(addedMatch.getMatchID());

            assertEquals(addedMatch, foundMatch);

            assertEquals(addedMatch.getMatchDate(), match.getMatchDate());
            assertEquals(addedMatch.getTicketsPrice(), match.getTicketsPrice());
            assertEquals(addedMatch.getCapacity(), match.getCapacity());
            assertEquals(addedMatch.getSoldUnits(), match.getSoldUnits());
            assertEquals(addedMatch.getVisitingTeam(), match.getVisitingTeam());
            assertEquals(addedMatch.getRegisterDate(), match.getRegisterDate());


        } finally {
            if (addedMatch != null) {
                removeMatch(addedMatch.getMatchID());
            }
        }

    }

    @Test
    public void testAddInvalidMatch() {
        //Caso de añadir un partido 5 días antes de la fecha actual
        Assertions.assertThrows(InputValidationException.class, () -> {
            Match match = getValidMatch();
            match.setMatchDate(LocalDateTime.now().minusDays(5));
            matchService.addMatch(match);
            removeMatch(match.getMatchID());
        });

        //Caso de añadir un partido con precio negativo
        Assertions.assertThrows(InputValidationException.class, () -> {
            Match match = getValidMatch();
            match.setTicketsPrice(-250);
            matchService.addMatch(match);
            removeMatch(match.getMatchID());
        });

        //Caso de añadir un partido con capacidad negativa
        Assertions.assertThrows(InputValidationException.class, () -> {
            Match match = getValidMatch();
            match.setCapacity(-60000);
            matchService.addMatch(match);
            removeMatch(match.getMatchID());
        });

    }

    @Test
    public void testFindNonExistentMatch() {
        Assertions.assertThrows(InstanceNotFoundException.class, () -> matchService.findMatch(NON_EXISTENT_MATCH_ID));
    }

    @Test
    public void testFindValidMatchesBetweenTwoDates() {
        List<Match> mymatches = new ArrayList<>();
        List<Match> addedmatches = new ArrayList<>();
        List<Match> foundmatches;

        mymatches.add(new Match(LocalDateTime.now().plusDays(1).withNano(0), 250, 60000, "Real Madrid"));
        mymatches.add(new Match(LocalDateTime.now().plusDays(7).withNano(0), 250, 60000, "Atlético de Madrid"));
        mymatches.add(new Match(LocalDateTime.now().plusDays(14).withNano(0), 250, 60000, "FC Barcelona"));
        mymatches.add(new Match(LocalDateTime.now().plusDays(21).withNano(0), 250, 60000, "Sevilla FC"));

        try {
            for (Match match : mymatches) {
                addedmatches.add(matchService.addMatch(match));
            }
            foundmatches = matchService.findMatches(LocalDateTime.now(), LocalDateTime.now().plusDays(20));

            for (int i = 0; i < foundmatches.size(); i++) {
                assertEquals(foundmatches.get(i).getMatchDate(), addedmatches.get(i).getMatchDate());
                assertEquals(foundmatches.get(i).getTicketsPrice(), addedmatches.get(i).getTicketsPrice());
                assertEquals(foundmatches.get(i).getCapacity(), addedmatches.get(i).getCapacity());
                assertEquals(foundmatches.get(i).getSoldUnits(), addedmatches.get(i).getSoldUnits());
                assertEquals(foundmatches.get(i).getVisitingTeam(), addedmatches.get(i).getVisitingTeam());
            }

        } catch (InputValidationException e) {
            e.printStackTrace();
        } finally {
            for (Match match : addedmatches) {
                removeMatch(match.getMatchID());
            }
        }
    }
    @Test
    public void testInvalidFindDate() {
        Assertions.assertThrows(InputValidationException.class, () -> matchService.findMatches(null, LocalDateTime.now()));     //Caso de pasar una fecha nula
        Assertions.assertThrows(InputValidationException.class, () -> matchService.findMatches(LocalDateTime.now().plusDays(20), LocalDateTime.now()));    //Caso de pasar una fecha final menor que una inicial
    }

    @Test
    public void buyTicketsAndFindPurchase() throws MatchNotAvailableException, InstanceNotFoundException, NotEnoughTicketsException, InputValidationException {

        Match addedMatch = createMatch(getValidMatch());
        Match foundMatch = null;
        Purchase purchase = null;

        try {
            purchase = matchService.buyTickets(USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 2, addedMatch.getMatchID());
            foundMatch = matchService.findMatch(addedMatch.getMatchID());

            Purchase foundPurchase = findPurchase(purchase.getPurchaseID());

            assertEquals(purchase, foundPurchase);
            assertEquals(USER_EMAIL, foundPurchase.getUserEmail());
            assertEquals(VALID_CREDIT_CARD_NUMBER, foundPurchase.getBankCard());
            assertEquals(purchase.getPurchaseDate(), foundPurchase.getPurchaseDate());
            assertEquals(purchase.getMatchID(), foundPurchase.getMatchID());
            assertEquals(purchase.getUnits(), foundPurchase.getUnits());
            assertEquals(purchase.getCollected(),foundPurchase.getCollected());

            //Comprobamos que el update de Match actualiza el número de entradas totales
            assertEquals(purchase.getUnits(), foundMatch.getSoldUnits());

        } finally {
            if (purchase != null) {
                removePurchase(purchase.getPurchaseID());
            }
            removeMatch(addedMatch.getMatchID());
        }
    }
    @Test
    public void testBuyNonExistentTicket() {
        assertThrows(InstanceNotFoundException.class, () -> {
            Purchase purchase = matchService.buyTickets(USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 2, NON_EXISTENT_MATCH_ID);
            removePurchase(purchase.getPurchaseID());
        });
    }

    @Test
    public void testFindNonExistentPurchase() {
        assertThrows(InstanceNotFoundException.class, () -> findPurchase(NON_EXISTENT_PURCHASE_ID));
    }

    @Test
    public void testBuyTicketWithInvalidCreditCard() {
        Match match = createMatch(getValidMatch());
        try {
            assertThrows(InputValidationException.class, () -> {
                Purchase purchase = matchService.buyTickets(USER_EMAIL, INVALID_CREDIT_CARD_NUMBER, 1, match.getMatchID());
                removePurchase(purchase.getPurchaseID());
            });

        } finally {
            removeMatch(match.getMatchID());
        }
    }

    @Test
    public void notEnoughTickets(){
        Match match1 = createMatch( new Match(LocalDateTime.now().plusDays(1).withNano(0), 20, 6, "Real Madrid"));
        try{
            assertThrows(NotEnoughTicketsException.class, () -> {
                Purchase purchase = matchService.buyTickets(USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 7, match1.getMatchID());
                removePurchase(purchase.getPurchaseID());
            });
        } finally {
            removeMatch(match1.getMatchID());
        }


    }
    @Test
    public void testInvalidDate() {
        Match match1 = createMatch( new Match(LocalDateTime.now().plusDays(1).withNano(0), 250, 60000, "Real Madrid"));

        try{
            match1.setMatchDate(LocalDateTime.now().minusDays(4).withNano(0));
            updateMatch(match1);
            assertThrows(MatchNotAvailableException.class, () -> {
                Purchase purchase = matchService.buyTickets(USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 2, match1.getMatchID());
                removePurchase(purchase.getPurchaseID());
            });
        } finally {
            removeMatch(match1.getMatchID());
        }

    }
    @Test
    public void testBuyTicketWithInvalidEmail() {
        Match match = createMatch(getValidMatch());
        try {
            assertThrows(InputValidationException.class, () -> {
                Purchase purchase = matchService.buyTickets("ABC123", VALID_CREDIT_CARD_NUMBER, 1, match.getMatchID());
                removePurchase(purchase.getPurchaseID());
            });

        } finally {
            removeMatch(match.getMatchID());
        }
    }

    @Test
    public void getPurchasesByUserValid() throws InstanceNotFoundException, InputValidationException, MatchNotAvailableException, NotEnoughTicketsException {
        List<Purchase> purchases = new ArrayList<>();
        List<Purchase> foundPurchases;

        Match match1 = new Match(LocalDateTime.now().plusDays(1).withNano(0), 250, 60000, "Real Madrid");
        Match match2 = new Match(LocalDateTime.now().plusDays(7).withNano(0), 250, 60000, "Atlético de Madrid");
        Match match3 = new Match(LocalDateTime.now().plusDays(14).withNano(0), 250, 60000, "FC Barcelona");

        Match addedMatch1 = matchService.addMatch(match1);
        Match addedMatch2 = matchService.addMatch(match2);
        Match addedMatch3 = matchService.addMatch(match3);

        Purchase purchase1 = matchService.buyTickets(USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1,addedMatch1.getMatchID());
        Purchase purchase2 = matchService.buyTickets(USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1,addedMatch2.getMatchID());
        Purchase purchase3 = matchService.buyTickets("wstestuser@gmail.com",VALID_CREDIT_CARD_NUMBER,1,addedMatch3.getMatchID());

        purchases.add(purchase1);
        purchases.add(purchase2);
        purchases.add(purchase3);

        foundPurchases = matchService.findUserPurchases(USER_EMAIL);

        try {
            for (int i = 0; i < foundPurchases.size(); i++){
                Purchase tmp = purchases.get(i);
                Purchase tmp2 = foundPurchases.get(i);

                assertEquals(tmp.getPurchaseID(),tmp2.getPurchaseID());
                assertEquals(tmp.getUserEmail(),tmp2.getUserEmail());
                assertEquals(tmp.getBankCard(),tmp2.getBankCard());
                assertEquals(tmp.getPurchaseDate(),tmp2.getPurchaseDate());
                assertEquals(tmp.getMatchID(),tmp2.getMatchID());
                assertEquals(tmp.getUnits(),tmp2.getUnits());
                assertEquals(tmp.getCollected(),tmp2.getCollected());
            }
        } finally {
            removePurchase(purchase1.getPurchaseID());
            removePurchase(purchase2.getPurchaseID());
            removePurchase(purchase3.getPurchaseID());
            removeMatch(addedMatch1.getMatchID());
            removeMatch(addedMatch2.getMatchID());
            removeMatch(addedMatch3.getMatchID());
        }

    }

    @Test
    public void testCollectedTicket() throws InstanceNotFoundException, InputValidationException, MatchNotAvailableException, NotEnoughTicketsException, IncorrectBankCardException, AlreadyCollectedException {
        Match match = createMatch(getValidMatch());
        Purchase purchase = matchService.buyTickets(USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1,match.getMatchID());
        try {
            matchService.collectedTicket(purchase.getPurchaseID(), VALID_CREDIT_CARD_NUMBER);
            Purchase foundPurchase = findPurchase(purchase.getPurchaseID());
            assertTrue(foundPurchase.getCollected());
        } finally {
            removePurchase(purchase.getPurchaseID());
            removeMatch(match.getMatchID());
        }
    }

    @Test
    public void testCollectedTicketWithInvalidPurchaseID() {
        assertThrows(InputValidationException.class, () -> {
            matchService.collectedTicket(-1L, VALID_CREDIT_CARD_NUMBER);
        });
    }

    @Test
    public void testCollectedTicketWithInvalidBankCard() throws MatchNotAvailableException, InstanceNotFoundException, NotEnoughTicketsException, InputValidationException {
        Match match = createMatch(getValidMatch());
        Purchase purchase = matchService.buyTickets(USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1,match.getMatchID());
        try {
            assertThrows(InputValidationException.class, () -> {
                matchService.collectedTicket(purchase.getPurchaseID(), null);
            });
            assertThrows(InputValidationException.class, () -> {
                matchService.collectedTicket(purchase.getPurchaseID(), "");
            });
            assertThrows(InputValidationException.class, () -> {
                matchService.collectedTicket(purchase.getPurchaseID(), "1234-1234-1234-123");
            });
        } finally {
            removePurchase(purchase.getPurchaseID());
            removeMatch(match.getMatchID());
        }
    }

    @Test
    public void testCollectedTicketInstanceNotFound() throws MatchNotAvailableException, InstanceNotFoundException, NotEnoughTicketsException, InputValidationException {
        Match match = createMatch(getValidMatch());
        Purchase purchase = matchService.buyTickets(USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1,match.getMatchID());
        try {
            assertThrows(InstanceNotFoundException.class, () -> {
                matchService.collectedTicket(purchase.getPurchaseID()+1, VALID_CREDIT_CARD_NUMBER);
            });
        } finally {
            removePurchase(purchase.getPurchaseID());
            removeMatch(match.getMatchID());
        }
    }

    @Test
    public void testCollectedTicketAlreadyCollected() throws MatchNotAvailableException, InstanceNotFoundException, NotEnoughTicketsException, InputValidationException, IncorrectBankCardException, AlreadyCollectedException {
        Match match = createMatch(getValidMatch());
        Purchase purchase = matchService.buyTickets(USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1,match.getMatchID());
        try {
            matchService.collectedTicket(purchase.getPurchaseID(), VALID_CREDIT_CARD_NUMBER);
            assertThrows(AlreadyCollectedException.class, () -> {
                matchService.collectedTicket(purchase.getPurchaseID(), VALID_CREDIT_CARD_NUMBER);
            });
        } finally {
            removePurchase(purchase.getPurchaseID());
            removeMatch(match.getMatchID());
        }
    }

    @Test
    public void testCollectedTicketIncorrectBankCard() throws MatchNotAvailableException, InstanceNotFoundException, NotEnoughTicketsException, InputValidationException, IncorrectBankCardException, AlreadyCollectedException {
        Match match = createMatch(getValidMatch());
        Purchase purchase = matchService.buyTickets(USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1,match.getMatchID());
        try {
            assertThrows(IncorrectBankCardException.class, () -> {
                matchService.collectedTicket(purchase.getPurchaseID(), "1234567890123457");
            });
        } finally {
            removePurchase(purchase.getPurchaseID());
            removeMatch(match.getMatchID());
        }
    }
}


