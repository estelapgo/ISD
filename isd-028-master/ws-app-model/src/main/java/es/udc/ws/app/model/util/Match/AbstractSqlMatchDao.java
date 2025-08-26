package es.udc.ws.app.model.util.Match;

import es.udc.ws.util.exceptions.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlMatchDao implements SqlMatchDao {

    protected AbstractSqlMatchDao() {

    }

    @Override
    public Match find(Connection connection, Long matchId) throws InstanceNotFoundException {
        String queryString = "SELECT matchdate,ticketsprice,capacity,visitingteam, soldunits, registerdate"
                + " FROM `match` WHERE idmatch = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, matchId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(matchId, Match.class.getName());
            }

            i = 1;
            LocalDateTime matchDate = resultSet.getTimestamp(i++).toLocalDateTime();
            double ticketsPrice = resultSet.getDouble(i++);
            int capacity = resultSet.getInt(i++);
            String visitingTeam = resultSet.getString(i++);
            int soldUnits = resultSet.getInt(i++);
            LocalDateTime registerDate = resultSet.getTimestamp(i++).toLocalDateTime();

            return new Match(matchId, matchDate, ticketsPrice, capacity, soldUnits, registerDate, visitingTeam);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Match match) throws InstanceNotFoundException{
        String queryString = "UPDATE `match`"
                + " SET matchdate = ?, ticketsprice = ?, capacity = ?, visitingteam = ?, soldunits = ?"
                + " WHERE idmatch = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(match.getMatchDate()));
            preparedStatement.setDouble(i++, match.getTicketsPrice());
            preparedStatement.setInt(i++, match.getCapacity());
            preparedStatement.setString(i++, match.getVisitingTeam());
            preparedStatement.setInt(i++, match.getSoldUnits());
            preparedStatement.setLong(i++, match.getMatchID());

            int updateRows = preparedStatement.executeUpdate();

            if(updateRows == 0) {
                throw new InstanceNotFoundException(match.getMatchID(),
                        Match.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long matchId) {
        String queryString = "DELETE FROM `match` WHERE idmatch = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, matchId);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(matchId, Match.class.getName());
            }

        } catch (SQLException | InstanceNotFoundException e) {
            e.printStackTrace();
        }
    }

        @Override
    public List<Match> findMatches(Connection connection, LocalDateTime initDate, LocalDateTime endDate){
        String queryString = "SELECT idmatch,matchdate,ticketsprice,capacity,visitingTeam,registerdate,soldunits"
                + " FROM `match` WHERE matchdate > ? and matchdate < ? ORDER BY matchdate";

        ArrayList<Match> matches = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i = 1;

            preparedStatement.setTimestamp(i++,Timestamp.valueOf(initDate));
            preparedStatement.setTimestamp(i++,Timestamp.valueOf(endDate));

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                i = 1;
                Long matchId = resultSet.getLong(i++);
                Timestamp matchdate = resultSet.getTimestamp(i++);
                Double ticketsPrice = resultSet.getDouble(i++);
                Integer capacity = resultSet.getInt(i++);
                String visitingTeam = resultSet.getString(i++);
                Timestamp registerDate = resultSet.getTimestamp(i++);
                Integer soldUnits = resultSet.getInt(i++);

                if(initDate.isBefore(matchdate.toLocalDateTime()) &&
                        endDate.isAfter(matchdate.toLocalDateTime())){
                    matches.add(new Match(matchId,matchdate.toLocalDateTime(),ticketsPrice,capacity,
                                soldUnits,registerDate.toLocalDateTime(),visitingTeam));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        /*if(matches.isEmpty()){
            System.out.println("No se ha encontrado ningún partido entre esas dos fechas");;
        }*/

        return matches;
    }
}
