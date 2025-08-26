package es.udc.ws.app.model.util.matchservice.exceptions;

public class AlreadyCollectedException extends Exception{
        public Long purchaseID;

        public AlreadyCollectedException(Long purchaseID){
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
