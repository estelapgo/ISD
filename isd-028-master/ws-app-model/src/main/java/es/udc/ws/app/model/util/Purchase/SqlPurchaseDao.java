package es.udc.ws.app.model.util.Purchase;


import es.udc.ws.util.exceptions.*;
import java.sql.Connection;
import java.util.List;

public interface SqlPurchaseDao {

    public Purchase create(Connection connection,Purchase purchase);

    public Purchase find(Connection connection,Long purchaseId) throws InstanceNotFoundException;

    public void update(Connection connection,Purchase purchase) throws InstanceNotFoundException;

    public List<Purchase> findByUser(Connection connection,String userEmail);

    public void remove(Connection connection,Long purchaseId) throws InstanceNotFoundException;
}
