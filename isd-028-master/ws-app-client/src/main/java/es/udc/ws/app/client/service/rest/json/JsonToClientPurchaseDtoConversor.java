package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonToClientPurchaseDtoConversor {
    public static ObjectNode toObjectNode(ClientPurchaseDto purchase) {
        ObjectNode purchaseObject = JsonNodeFactory.instance.objectNode();

        if (purchase.getPurchaseId() != null) {
            purchaseObject.put("purchaseId", purchase.getPurchaseId());
        }
        purchaseObject.put("userEmail", purchase.getUserEmail());
        purchaseObject.put("creditCardNumber", purchase.getBankCard());
        purchaseObject.put("purchaseDate", purchase.getPurchaseDate().toString());
        purchaseObject.put("matchId", purchase.getMatchId());
        purchaseObject.put("units", purchase.getUnits());
        purchaseObject.put("collected", purchase.getCollected());

        return purchaseObject;
    }

    public static ClientPurchaseDto toClientPurchaseDto(InputStream jsonPurchase) {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPurchase);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientPurchaseDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientPurchaseDto> toClientPurchaseDtos (InputStream jsonPurchases) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPurchases);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode purchasesArray = (ArrayNode) rootNode;
                List<ClientPurchaseDto> purchaseDtos = new ArrayList<>(purchasesArray.size());
                for (JsonNode purchaseNode : purchasesArray) {
                    purchaseDtos.add(toClientPurchaseDto(purchaseNode));
                }
                return purchaseDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static ClientPurchaseDto toClientPurchaseDto(JsonNode purchaseNode) throws ParsingException {
        if (purchaseNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode purchaseObject = (ObjectNode) purchaseNode;

            JsonNode purchaseIdNode = purchaseObject.get("purchaseId");
            Long purchaseId = (purchaseIdNode != null) ? purchaseIdNode.longValue() : null;

            String userEmail = purchaseObject.get("userEmail").textValue().trim();
            String lastDigitsBankCard = purchaseObject.get("bankCard").textValue();
            LocalDateTime purchaseDate = LocalDateTime.parse(purchaseObject.get("purchaseDate").textValue().trim());
            Long matchId = purchaseObject.get("matchId").longValue();
            int units = purchaseObject.get("units").intValue();
            Boolean collected = purchaseObject.get("collected").booleanValue();

            return new ClientPurchaseDto(purchaseId, userEmail, lastDigitsBankCard, purchaseDate, matchId, units, collected);
        }
    }
}
