package es.udc.ws.app.model.util.matchservice;

import es.udc.ws.app.model.util.Match.Match;
import es.udc.ws.app.model.util.Purchase.Purchase;
import es.udc.ws.util.exceptions.*;
import es.udc.ws.app.model.util.matchservice.exceptions.*;
import java.time.LocalDateTime;
import java.util.List;


public interface MatchService {

    public Match addMatch(Match match) throws InputValidationException;
    public Match findMatch(Long matchID) throws InstanceNotFoundException;

    public List<Match> findMatches(LocalDateTime initDate, LocalDateTime endDate) throws InputValidationException;

    public Purchase buyTickets(String userEmail, String creditCardNumber, int numTickets, Long matchID)
            throws InputValidationException, InstanceNotFoundException,
            MatchNotAvailableException, NotEnoughTicketsException;
    public List<Purchase> findUserPurchases(String userEmail) throws InputValidationException;

    public void collectedTicket(Long purchaseID, String bankCard) throws InputValidationException,
            InstanceNotFoundException, AlreadyCollectedException, IncorrectBankCardException;
}
