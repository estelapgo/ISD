package es.udc.ws.app.model.util.matchservice.exceptions;

public class IncorrectBankCardException extends Exception{

    public Long purchaseID;

    public IncorrectBankCardException(Long purchaseID){
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
