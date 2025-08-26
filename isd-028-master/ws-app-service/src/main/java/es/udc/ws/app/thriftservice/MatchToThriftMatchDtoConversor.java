package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.util.Match.Match;
import es.udc.ws.app.thrift.ThriftMatchDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchToThriftMatchDtoConversor {

    public static Match toMatch (ThriftMatchDto match) {
        return new Match(match.getMatchId(),LocalDateTime.parse(match.getMatchDate()),
                match.getTicketsPrice(),match.getCapacity(),match.getSoldUnits(),match.getVisitingTeam());
    }

    public static List<ThriftMatchDto> toThriftMatchDtos(List<Match> matches){
        List<ThriftMatchDto> dtos = new ArrayList<>(matches.size());

        for (Match match:matches) {
            dtos.add(toThriftMatchDto(match));
        }

        return dtos;
    }

    public static ThriftMatchDto toThriftMatchDto(Match match) {
        return new ThriftMatchDto(match.getMatchID(),match.getMatchDate().toString(),match.getTicketsPrice(),
                match.getCapacity(),match.getSoldUnits(),match.getVisitingTeam());
    }
}

