package es.udc.ws.app.client.service.exceptions;

public class ClientAlreadyCollectedException extends Exception {
    public Long purchaseID;

    public ClientAlreadyCollectedException(Long purchaseID){
        super("Purchase with ID = " + purchaseID + " has already been collected");
        this.purchaseID = purchaseID;
    }

    public Long getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Long purchaseID) {
        this.purchaseID = purchaseID;
    }
}
