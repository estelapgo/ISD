package es.udc.ws.app.thriftservice;
import es.udc.ws.app.model.util.Match.Match;
import es.udc.ws.app.model.util.Purchase.Purchase;
import es.udc.ws.app.model.util.matchservice.MatchServiceFactory;
import es.udc.ws.app.model.util.matchservice.exceptions.MatchNotAvailableException;
import es.udc.ws.app.model.util.matchservice.exceptions.NotEnoughTicketsException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftMatchServiceImpl implements ThriftMatchService.Iface{

    @Override
    public ThriftMatchDto addMatch(ThriftMatchDto matchDto) throws ThriftInputValidationException{
        Match match = MatchToThriftMatchDtoConversor.toMatch(matchDto);

        try {

            Match addedMatch = MatchServiceFactory.getService().addMatch(match);

            return MatchToThriftMatchDtoConversor.toThriftMatchDto(addedMatch);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public List<ThriftMatchDto> findMatches(String initDateS,String endDateS) throws ThriftInputValidationException{
        try {
            List<Match> matches = MatchServiceFactory.getService().findMatches(LocalDateTime.parse(initDateS),LocalDateTime.parse(endDateS));
            return MatchToThriftMatchDtoConversor.toThriftMatchDtos(matches);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public ThriftMatchDto findMatch(long matchId) throws ThriftInstanceNotFoundException {
        try {
            Match match = MatchServiceFactory.getService().findMatch(matchId);
            return MatchToThriftMatchDtoConversor.toThriftMatchDto(match);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType());
        }
    }

    @Override
    public ThriftPurchaseDto buyTickets(String userEmail, String creditCardNumber,int numtickets,long matchId)
            throws ThriftInputValidationException, ThriftInstanceNotFoundException,
            ThriftMatchNotAvaliableException,ThriftNotEnoughTicketsException {
        try {
            Purchase purchase = MatchServiceFactory.getService().buyTickets(userEmail,creditCardNumber,numtickets,matchId);
            return PurchaseToThriftPurchaseDtoConversor.toThriftPurchaseDto(purchase);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),e.getInstanceType());
        } catch (MatchNotAvailableException e){
            throw new ThriftMatchNotAvaliableException(e.toString());
        } catch (NotEnoughTicketsException e){
            throw new ThriftNotEnoughTicketsException(e.matchID,e.getTicketsAvailable(),e.getTicketsRequested());
        }
    }
}
