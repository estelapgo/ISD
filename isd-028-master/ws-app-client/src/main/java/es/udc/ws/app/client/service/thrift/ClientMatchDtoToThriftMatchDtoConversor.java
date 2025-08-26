package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.thrift.ThriftMatchDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientMatchDtoToThriftMatchDtoConversor {

    public static ThriftMatchDto tothriftMatchDto(ClientMatchDto clientMatchDto) {

        Long matchId = clientMatchDto.getMatchId();

        return new ThriftMatchDto(
                matchId == null ? -1 : matchId.longValue(),
                clientMatchDto.getMatchDate().toString(),clientMatchDto.getTicketsPrice(),
                clientMatchDto.getCapacity(),clientMatchDto.getSoldUnits(),clientMatchDto.getVisitingTeam());
    }

    public static List<ClientMatchDto> toClientMatchDtos(List<ThriftMatchDto> matches) {
        List<ClientMatchDto> clientMatchDtos = new ArrayList<>(matches.size());

        for(ThriftMatchDto match: matches){
            clientMatchDtos.add(toClientMatchDto(match));
        }

        return clientMatchDtos;
    }

    public static ClientMatchDto toClientMatchDto(ThriftMatchDto match) {
        return new ClientMatchDto(
                match.getMatchId(),
                LocalDateTime.parse(match.getMatchDate()),
                match.getTicketsPrice(),
                match.getCapacity(),
                match.getSoldUnits(),
                match.getVisitingTeam()
        );
    }
}
