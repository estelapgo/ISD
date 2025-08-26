package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("AlreadyCollected")) {
                    return toAlreadyCollectedException(rootNode);
                } else if (errorType.equals("IncorrectBankCard")) {
                    return toIncorrectBankCardException(rootNode);
                } else if (errorType.equals("MatchNotAvaliable")) {
                    return toMatchNotAvailableException(rootNode);
                } else if (errorType.equals("NotEnoughTickets")) {
                    return toNoteEnoughTicketsException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientAlreadyCollectedException toAlreadyCollectedException(JsonNode rootNode) {
        Long id = rootNode.get("purchaseId").longValue();
        return new ClientAlreadyCollectedException(id);
    }

    private static ClientMatchNotAvailableException toMatchNotAvailableException(JsonNode rootNode){
        LocalDateTime date = LocalDateTime.parse(rootNode.get("matchDate").asText());
        return new ClientMatchNotAvailableException(date);
    }

    private static ClientNotEnoughTicketsException toNoteEnoughTicketsException(JsonNode rootNode){
        Long matchId = rootNode.get("matchId").longValue();
        int ticketsRequested = rootNode.get("requestedTickets").intValue();
        int plazasDisp = rootNode.get("avaliableTickets").intValue();
        return new ClientNotEnoughTicketsException(matchId, ticketsRequested , plazasDisp);
    }

    private static ClientIncorrectBankCardException toIncorrectBankCardException(JsonNode rootNode) {
        Long purchaseId = rootNode.get("purchaseId").longValue();
        return new ClientIncorrectBankCardException(purchaseId);
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
//                if (errorType.equals("LateCancelationException")) {
//                    return toLateCancelationException(rootNode);
//                }else if (errorType.equals("LateRegisterException")) {
//                    return toLateRegisterException(rootNode);
//                }else {
                throw new ParsingException("Unrecognized error type: " + errorType);
//                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}