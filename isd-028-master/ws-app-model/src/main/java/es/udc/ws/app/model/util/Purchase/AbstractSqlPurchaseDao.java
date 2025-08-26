package es.udc.ws.app.model.util.Purchase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import es.udc.ws.util.exceptions.*;

public abstract class AbstractSqlPurchaseDao implements SqlPurchaseDao{
    protected AbstractSqlPurchaseDao(){
    }

    @Override
    public Purchase find(Connection connection, Long purchaseId) throws InstanceNotFoundException {
        String queryString = "SELECT useremail,bankcard,purchasedate,matchid,units,collected" +
                " FROM purchase WHERE idpurchase = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++,purchaseId.longValue());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InstanceNotFoundException(purchaseId,Purchase.class.getName());
            }

            i = 1;
            String userEmail = resultSet.getString(i++);
            String bankCard = resultSet.getString(i++);
            LocalDateTime purchaseDate = resultSet.getTimestamp(i++).toLocalDateTime();
            Long matchId = resultSet.getLong(i++);
            int units = resultSet.getInt(i++);
            Boolean collected = resultSet.getBoolean(i++);

            return new Purchase(purchaseId,userEmail,bankCard,purchaseDate,matchId,units,collected);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Purchase purchase) throws InstanceNotFoundException {
        String queryString = "UPDATE purchase" + " SET useremail = ?, bankcard = ?, purchasedate = ?, matchid = ?," +
                " units = ?, collected = ? WHERE idpurchase = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++,purchase.getUserEmail());
            preparedStatement.setString(i++,purchase.getBankCard());
            preparedStatement.setTimestamp(i++,Timestamp.valueOf(purchase.getPurchaseDate()));
            preparedStatement.setLong(i++,purchase.getMatchID());
            preparedStatement.setInt(i++,purchase.getUnits());
            preparedStatement.setBoolean(i++,purchase.getCollected());
            preparedStatement.setLong(i++,purchase.getPurchaseID());

            int updatedRows = preparedStatement.executeUpdate();

            if(updatedRows == 0) {
                throw new InstanceNotFoundException(purchase.getPurchaseID(),Purchase.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public List<Purchase> findByUser(Connection connection, String userEmail) {
        String queryString = "SELECT idpurchase, useremail, bankcard, purchasedate, matchid," +
                " units, collected FROM purchase WHERE useremail = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;

            preparedStatement.setString(i++,userEmail);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Purchase> purchases = new ArrayList<>();

            while (resultSet.next()) {
                i = 1;

                Long purchaseID = resultSet.getLong(i++);
                String email = resultSet.getString(i++);
                String creditCard = resultSet.getString(i++);
                Timestamp purchaseDate = resultSet.getTimestamp(i++);
                Long matchID = resultSet.getLong(i++);
                int units = resultSet.getInt(i++);
                Boolean collected = resultSet.getBoolean(i++);

                purchases.add(new Purchase(purchaseID,email,creditCard,purchaseDate.toLocalDateTime(),matchID,units,collected));
            }

            return purchases;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long purchaseId) throws InstanceNotFoundException {

        String queryString = "DELETE FROM purchase WHERE" + " idpurchase = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, purchaseId);

            int removeRows = preparedStatement.executeUpdate();

            if (removeRows == 0) {
                throw new InstanceNotFoundException(purchaseId,
                        Purchase.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

