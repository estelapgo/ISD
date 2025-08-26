package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.thrift.*;
import es.udc.ws.app.client.service.ClientMatchService;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.app.client.service.exceptions.ClientIncorrectBankCardException;
import es.udc.ws.app.client.service.exceptions.ClientMatchNotAvailableException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughTicketsException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftClientMatchService implements ClientMatchService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientMatchService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
    @Override
    public Long addMatch(ClientMatchDto matchDto) throws InputValidationException {
        ThriftMatchService.Client client = getClient();

        try(TTransport transport = client.getInputProtocol().getTransport()){

            transport.open();

            return client.addMatch(ClientMatchDtoToThriftMatchDtoConversor.tothriftMatchDto(matchDto)).getMatchId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientMatchDto findMatch(Long matchId) throws InstanceNotFoundException {
        ThriftMatchService.Client client = getClient();

        try(TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientMatchDtoToThriftMatchDtoConversor.toClientMatchDto(client.findMatch(matchId.longValue()));
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(),e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientMatchDto> findMatches(LocalDateTime endDate) throws InputValidationException {
        ThriftMatchService.Client client = getClient();

        try(TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientMatchDtoToThriftMatchDtoConversor.toClientMatchDtos(client.findMatches(LocalDateTime.now().withNano(0).toString(),endDate.toString()));
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPurchaseDto buyTickets(String userEmail, String creditCardNumber, int numtickets, Long matchId) throws InputValidationException, InstanceNotFoundException, ClientMatchNotAvailableException, ClientNotEnoughTicketsException {
        ThriftMatchService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPurchaseDtoToThriftPurchaseDtoConversor.toClientPurchaseDto(client.buyTickets(userEmail,creditCardNumber,numtickets,matchId));
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(),e.getInstanceType());
        } catch (ThriftMatchNotAvaliableException e) {
            throw new ClientMatchNotAvailableException(LocalDateTime.parse(e.getMatchDate().substring(108,127)));
        } catch (ThriftNotEnoughTicketsException e) {
            throw new ClientNotEnoughTicketsException(e.getMatchId(),e.getRequestedTickets(),e.getTicketsAvaliable());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPurchaseDto> findUserPurchases(String userEmail) throws InputValidationException {
        return null;
    }

    @Override
    public void collectedTicket(Long purchaseId, String bankCard) throws InputValidationException, InstanceNotFoundException, ClientAlreadyCollectedException, ClientIncorrectBankCardException {

    }

    private ThriftMatchService.Client getClient() {
        try {
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftMatchService.Client(protocol);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }
}
