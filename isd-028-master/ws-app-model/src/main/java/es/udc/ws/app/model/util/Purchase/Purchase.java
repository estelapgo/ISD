package es.udc.ws.app.model.util.Purchase;

import java.time.LocalDateTime;
import java.util.Objects;

public class Purchase {
    private Long purchaseID;
    private String userEmail;
    private String bankCard;
    private LocalDateTime purchaseDate;
    private Long matchID;
    private int units;              //num de entradas en la compra
    private Boolean collected;

    public Purchase(String userEmail, String bankCard,LocalDateTime purchaseDate, Long matchID, int units) {
        this.userEmail = userEmail;
        this.bankCard = bankCard;
        this.purchaseDate = purchaseDate;
        this.matchID = matchID;
        this.units = units;
        this.collected = false;
    }

    public Purchase(Long purchaseID, String userEmail, String bankCard,LocalDateTime purchaseDate, Long matchID, int units) {
        this(userEmail, bankCard, purchaseDate, matchID, units);
        this.purchaseID = purchaseID;
    }

    public Purchase(Long purchaseID, String userEmail, String bankCard,LocalDateTime purchaseDate, Long matchID, int units, Boolean collected) {
        this(purchaseID, userEmail, bankCard, purchaseDate, matchID, units);
        this.collected = collected;
    }

    //Setters y getters

    public Long getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Long purchaseID) {
        this.purchaseID = purchaseID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Long getMatchID() {
        return matchID;
    }

    public void setMatchID(Long matchID) {
        this.matchID = matchID;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public Boolean getCollected() {
        return collected;
    }

    public void setCollected(Boolean collected) {
        this.collected = collected;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Purchase purchase = (Purchase) obj;
        return purchaseID.equals(purchase.purchaseID) &&
                userEmail.equals(purchase.userEmail) &&
                bankCard.equals(purchase.bankCard) &&
                purchaseDate.equals(purchase.purchaseDate) &&
                matchID.equals(purchase.matchID) &&
                units == purchase.units &&
                collected.equals(purchase.collected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseID, userEmail, bankCard, purchaseDate, matchID, units, collected);
    }

}
