package es.udc.ws.app.model.util.Match;

import es.udc.ws.util.exceptions.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlMatchDao {
    public Match create(Connection connection,Match match);

    public void update(Connection connection,Match match) throws InstanceNotFoundException;

    public Match find(Connection connection,Long matchId) throws InstanceNotFoundException;

    public List<Match> findMatches(Connection connection, LocalDateTime initDate, LocalDateTime endDate);

    public void remove(Connection connection, Long matchId) throws InstanceNotFoundException;
}
