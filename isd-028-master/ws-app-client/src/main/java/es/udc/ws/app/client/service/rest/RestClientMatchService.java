package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientMatchService;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.app.client.service.exceptions.ClientIncorrectBankCardException;
import es.udc.ws.app.client.service.exceptions.ClientMatchNotAvailableException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughTicketsException;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientMatchDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientPurchaseDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http2.nio.support.DefaultAsyncPushConsumerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

public class RestClientMatchService implements ClientMatchService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientMatchService.endpointAddress";
    private String endpointAddress;
    @Override
    public Long addMatch(ClientMatchDto matchDto) throws InputValidationException {
        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "matches")
                    .bodyStream(toInputSteam(matchDto), ContentType.create("application/json"))
                    .execute().returnResponse();

            validateSatusCode(HttpStatus.SC_CREATED,response);

            return JsonToClientMatchDtoConversor.toClientMatchDto(response.getEntity().getContent()).getMatchId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientMatchDto findMatch(Long matchId) throws InstanceNotFoundException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "matches/" + matchId)
                    .execute().returnResponse();

            validateSatusCode(HttpStatus.SC_OK,response);

            return JsonToClientMatchDtoConversor.toClientMatchDto(response.getEntity().getContent());
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<ClientMatchDto> findMatches(LocalDateTime endDate) throws InputValidationException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() +
                            "matches?endDate=" + endDate.toString().substring(0,10))
                    .execute().returnResponse();

            validateSatusCode(HttpStatus.SC_OK,response);

            return JsonToClientMatchDtoConversor.toClientMatchDtos(response.getEntity().getContent());
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPurchaseDto buyTickets(String userEmail, String creditCardNumber, int numtickets, Long matchId) throws InputValidationException, InstanceNotFoundException, ClientMatchNotAvailableException, ClientNotEnoughTicketsException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "purchases")
                    .bodyForm(
                            Form.form().
                                    add("matchId", Long.toString(matchId)).
                                    add("numTickets",String.valueOf(numtickets)).
                                    add("creditCardNumber",creditCardNumber).
                                    add("userEmail",userEmail).
                                    build()).
                    execute().returnResponse();

            validateSatusCode(HttpStatus.SC_CREATED,response);

            return JsonToClientPurchaseDtoConversor.toClientPurchaseDto(response.getEntity().getContent());
        } catch (InputValidationException | InstanceNotFoundException | ClientMatchNotAvailableException |
                 ClientNotEnoughTicketsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPurchaseDto> findUserPurchases(String userEmail) throws InputValidationException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "purchases?userEmail=" + userEmail)
                    .execute().returnResponse();

            validateSatusCode(HttpStatus.SC_OK,response);

            return JsonToClientPurchaseDtoConversor.toClientPurchaseDtos(response.getEntity().getContent());
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void collectedTicket(Long purchaseId, String bankCard) throws InputValidationException, InstanceNotFoundException, ClientAlreadyCollectedException, ClientIncorrectBankCardException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "purchases/" + purchaseId
                            + "/collected").bodyForm(
                            Form.form().
                                    add("bankCard",bankCard).
                                    build()).
                    execute().returnResponse();
            validateSatusCode(HttpStatus.SC_NO_CONTENT,response);
        } catch (InputValidationException | InstanceNotFoundException | ClientAlreadyCollectedException |
                 ClientIncorrectBankCardException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress(){
        if(endpointAddress == null){
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputSteam(ClientMatchDto matchDto) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientMatchDtoConversor.toObjectNode(matchDto));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateSatusCode(int succesCode, ClassicHttpResponse response) throws Exception{
        try {
            int statusCode = response.getCode();

            if(statusCode == succesCode){
                return;
            }

            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
