package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientMatchDtoConversor {
    public static ObjectNode toObjectNode(ClientMatchDto match) {
        ObjectNode matchObject = JsonNodeFactory.instance.objectNode();

        if (match.getMatchId() != null) {
            matchObject.put("matchId", match.getMatchId());
        }

        matchObject.put("matchDate", match.getMatchDate().toString())
                .put("ticketsPrice", match.getTicketsPrice())
                .put("capacity", match.getCapacity())
                .put("ticketsAvaliable", match.getCapacity() - match.getSoldUnits())
                .put("visitingTeam", match.getVisitingTeam());

        return matchObject;
    }

    public static ClientMatchDto toClientMatchDto(InputStream jsonMatch) {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonMatch);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientMatchDto(rootNode);
            }
        }catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ClientMatchDto> toClientMatchDtos(InputStream jsonMatch) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonMatch);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode matchsArray = (ArrayNode) rootNode;
                List<ClientMatchDto> matchDtos = new ArrayList<>(matchsArray.size());
                for (JsonNode matchNode : matchsArray) {
                    matchDtos.add(toClientMatchDto(matchNode));
                }

                return matchDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientMatchDto toClientMatchDto(JsonNode matchNode) throws ParsingException {
        if (matchNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode matchObject = (ObjectNode) matchNode;

            JsonNode matchIdNode = matchObject.get("matchId");
            Long matchId = (matchIdNode != null) ? matchIdNode.longValue() : null;

            LocalDateTime matchDate = LocalDateTime.parse(matchObject.get("matchDate").textValue().trim());
            double ticketsPrice = matchObject.get("ticketsPrice").floatValue();
            int capacity = matchObject.get("capacity").intValue();
            int soldunits = matchObject.get("soldUnits").intValue();
            String visitingTeam = matchObject.get("visitingTeam").textValue().trim();


            return new ClientMatchDto(matchId, matchDate, ticketsPrice, capacity,soldunits,visitingTeam);
        }
    }
}
