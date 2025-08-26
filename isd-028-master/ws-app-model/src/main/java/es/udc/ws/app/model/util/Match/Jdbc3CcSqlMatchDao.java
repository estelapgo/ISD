package es.udc.ws.app.model.util.Match;

import java.sql.*;

public class Jdbc3CcSqlMatchDao extends AbstractSqlMatchDao{
    @Override
    public Match create(Connection connection, Match match) {

        String queryString = "INSERT INTO `match`"
                + " (matchdate,ticketsprice,capacity,soldunits,registerdate,visitingteam) "
                + "VALUES (?,?,?,?,?,?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setTimestamp(i++,Timestamp.valueOf(match.getMatchDate()));
            preparedStatement.setDouble(i++,match.getTicketsPrice());
            preparedStatement.setLong(i++,match.getCapacity());
            preparedStatement.setLong(i++,match.getSoldUnits());
            preparedStatement.setTimestamp(i++,Timestamp.valueOf(match.getRegisterDate()));
            preparedStatement.setString(i++,match.getVisitingTeam());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated keys"
                );
            }

            Long matchId = resultSet.getLong(1);

            return new Match(matchId,match.getMatchDate(),match.getTicketsPrice(),
                    match.getCapacity(),match.getSoldUnits(),match.getRegisterDate(),
                    match.getVisitingTeam());
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
