package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.app.client.service.exceptions.ClientIncorrectBankCardException;
import es.udc.ws.app.client.service.exceptions.ClientMatchNotAvailableException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughTicketsException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientMatchService {

    public Long addMatch(ClientMatchDto matchDto)
        throws InputValidationException;

    public ClientMatchDto findMatch(Long matchId)
            throws InstanceNotFoundException;

    public List<ClientMatchDto> findMatches(LocalDateTime endDate)
            throws InputValidationException;
    public ClientPurchaseDto buyTickets(String userEmail, String creditCardNumber, int numtickets, Long matchId)
            throws InputValidationException, InstanceNotFoundException,
            ClientMatchNotAvailableException, ClientNotEnoughTicketsException;
    public List<ClientPurchaseDto> findUserPurchases(String userEmail)
        throws InputValidationException;

    public void collectedTicket(Long purchaseId,String bankCard) throws InputValidationException,
            InstanceNotFoundException, ClientAlreadyCollectedException,
            ClientIncorrectBankCardException;
}
