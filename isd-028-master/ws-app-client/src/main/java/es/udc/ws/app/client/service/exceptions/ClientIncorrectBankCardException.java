package es.udc.ws.app.client.service.exceptions;

public class ClientIncorrectBankCardException extends Exception {
    public Long purchaseID;

    public ClientIncorrectBankCardException(Long purchaseID){
        super("Purchase with ID = " + purchaseID.toString() + " used different bank card" );
        this.purchaseID = purchaseID;
    }

    public Long getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Long purchaseID) {
        this.purchaseID = purchaseID;
    }
}
