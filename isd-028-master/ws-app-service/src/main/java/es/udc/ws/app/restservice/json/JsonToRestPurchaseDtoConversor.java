package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestPurchaseDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonToRestPurchaseDtoConversor {


    public static ObjectNode toObjectNode(RestPurchaseDto purchase) {

        ObjectNode purchaseNode = JsonNodeFactory.instance.objectNode();

        if(purchase.getPurchaseID() != null) {
            purchaseNode.put("purchaseId",purchase.getPurchaseID());
        }

        purchaseNode.put("userEmail",purchase.getUserEmail()).
                put("bankCard",purchase.getBankCard()).
                put("purchaseDate",purchase.getPurchaseDate().toString()).
                put("matchId",purchase.getMatchID()).
                put("units",purchase.getUnits()).
                put("collected",purchase.getCollected());

        return purchaseNode;
    }

    public static ArrayNode toArrayNode(List<RestPurchaseDto> purchaseDtos){

        ArrayNode purcahsesNode = JsonNodeFactory.instance.arrayNode();
        for(int i = 0; i < purchaseDtos.size(); i++) {
            RestPurchaseDto purchaseDto = purchaseDtos.get(i);
            ObjectNode purchaseObject = toObjectNode(purchaseDto);
            purcahsesNode.add(purchaseObject);
        }

        return purcahsesNode;
    }


    public static RestPurchaseDto toRestPurchaseDto(InputStream jsonPurchase) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPurchase);

            if(rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode purchaseObject = (ObjectNode) rootNode;

                JsonNode purchaseIdNode = purchaseObject.get("purchaseId");
                Long purchaseId = (purchaseIdNode != null) ? purchaseIdNode.longValue() : null;

                String userEmail = purchaseObject.get("userEmail").textValue().trim();
                String bankCard = purchaseObject.get("bankCard").textValue().trim();
                String purchaseDateS = purchaseObject.get("purchaseDate").textValue().trim();
                Long matchId = purchaseObject.get("matchId").longValue();
                int units = purchaseObject.get("units").asInt();
                boolean collected = purchaseObject.get("collected").asBoolean();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime purchaseDate = LocalDateTime.parse(purchaseDateS,formatter);

                return new RestPurchaseDto(purchaseId,userEmail,bankCard,purchaseDate,matchId,units,collected);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
