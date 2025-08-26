package es.udc.ws.app.model.util.Purchase;

import java.sql.*;



public class Jdbc3CcSqlPurchaseDao extends AbstractSqlPurchaseDao {

    @Override
    public Purchase create(Connection connection, Purchase purchase) {

        String queryString = "INSERT INTO purchase"
                + "(useremail, bankcard, purchasedate, matchid,"
                + " units,collected) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i= 1;
            preparedStatement.setString(i++, purchase.getUserEmail());
            preparedStatement.setString(i++, purchase.getBankCard());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(purchase.getPurchaseDate()));
            preparedStatement.setLong(i++, purchase.getMatchID());
            preparedStatement.setLong(i++, purchase.getUnits());
            preparedStatement.setBoolean(i++,purchase.getCollected());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()) {
                throw new SQLException(
                        "JDBC Driver did not return generated key"
                );
            }

            Long purchaseId = resultSet.getLong(1);

            return new Purchase(purchaseId,purchase.getUserEmail(), purchase.getBankCard(),
                    purchase.getPurchaseDate(), purchase.getMatchID(),
                    purchase.getUnits(), purchase.getCollected());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
