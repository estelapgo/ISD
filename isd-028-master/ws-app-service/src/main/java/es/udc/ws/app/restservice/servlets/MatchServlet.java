package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.util.Match.Match;
import es.udc.ws.app.model.util.matchservice.MatchServiceFactory;
import es.udc.ws.app.restservice.dto.MatchToRestMatchDtoConversor;
import es.udc.ws.app.restservice.dto.RestMatchDto;
import es.udc.ws.app.restservice.json.JsonToRestMatchDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchServlet extends RestHttpServletTemplate {
    public MatchServlet() {
    }

    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestMatchDto matchDto = JsonToRestMatchDtoConversor.toRestMatchDto(req.getInputStream());
        Match match = MatchToRestMatchDtoConversor.toMatch(matchDto);
        match = MatchServiceFactory.getService().addMatch(match);
        matchDto = MatchToRestMatchDtoConversor.toRestMatchDto(match);

        String url = ServletUtils.normalizePath(req.getRequestURL().toString());
        String matchURL = url + "/" + match.getMatchID();

        Map<String, String> headers = new HashMap(1);
        headers.put("Location", matchURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestMatchDtoConversor.toObjectNode(matchDto), headers);
    }

    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException, InstanceNotFoundException {
        String endDateS = req.getParameter("endDate");

        if (endDateS != null) {
            ServletUtils.checkEmptyPath(req);

            endDateS = endDateS.concat(" 00:00:00");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endDate = LocalDateTime.parse(endDateS, formatter);

            List<Match> matches = MatchServiceFactory.getService().findMatches(LocalDateTime.now().withNano(0), endDate);
            List<RestMatchDto> matchDtos = MatchToRestMatchDtoConversor.toRestMatchDtos(matches);

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestMatchDtoConversor.toArrayNode(matchDtos), null);
        } else {
            Long matchId = ServletUtils.getIdFromPath(req, "matchId");

            if (matchId != null) {
                Match match = MatchServiceFactory.getService().findMatch(matchId);
                RestMatchDto matchDto = MatchToRestMatchDtoConversor.toRestMatchDto(match);

                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestMatchDtoConversor.toObjectNode(matchDto), null);
            }
        }
    }
}