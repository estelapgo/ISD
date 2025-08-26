package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestMatchDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class JsonToRestMatchDtoConversor {

    public static ObjectNode toObjectNode(RestMatchDto match) {

        ObjectNode matchObject = JsonNodeFactory.instance.objectNode();

        matchObject.put("matchId", match.getMatchID()).
                put("matchDate", match.getMatchDate().toString()).
                put("ticketsPrice",match.getTicketsPrice()).
                put("capacity",match.getCapacity()).
                put("soldUnits",match.getSoldUnits()).
                put("visitingTeam",match.getVisitingTeam());

        return matchObject;
    }

    public static ArrayNode toArrayNode(List<RestMatchDto> matches) {

        ArrayNode matchNodes = JsonNodeFactory.instance.arrayNode();

        for(int i = 0; i < matches.size(); i++){
            RestMatchDto matchDto = matches.get(i);
            ObjectNode matchObject = toObjectNode(matchDto);
            matchNodes.add(matchObject);
        }

        return matchNodes;
    }

    public static RestMatchDto toRestMatchDto(InputStream jsonMatch) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonMatch);

            if(rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode matchObject = (ObjectNode) rootNode;

                JsonNode matchIdNode = matchObject.get("matchId");
                Long matchId = (matchIdNode != null) ? matchIdNode.longValue() : null;

                String matchDateS = matchObject.get("matchDate").textValue().trim();
                float ticketsPrice = matchObject.get("ticketsPrice").floatValue();
                int capacity = matchObject.get("capacity").intValue();
                String visitingTeam = matchObject.get("visitingTeam").textValue().trim();
                LocalDateTime matchDate = LocalDateTime.parse(matchDateS);

                return  new RestMatchDto(matchId,matchDate,ticketsPrice,capacity,visitingTeam);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
