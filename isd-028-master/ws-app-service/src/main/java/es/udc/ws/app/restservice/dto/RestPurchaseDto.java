package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestPurchaseDto {

    private Long purchaseID;
    private String userEmail;
    private String bankCard;
    private LocalDateTime purchaseDate;
    private Long matchID;
    private int units;
    private Boolean collected;

    public RestPurchaseDto(Long purchaseID, String userEmail, String bankCard, LocalDateTime purchaseDate, Long matchID, int units, Boolean collected) {
        this.purchaseID = purchaseID;
        this.userEmail = userEmail;
        this.bankCard = bankCard;
        this.purchaseDate = purchaseDate;
        this.matchID = matchID;
        this.units = units;
        this.collected = collected;
    }

    public Long getPurchaseID() {
        return purchaseID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getBankCard() {
        return bankCard;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public Long getMatchID() {
        return matchID;
    }

    public int getUnits() {
        return units;
    }

    public Boolean getCollected() {
        return collected;
    }

    public void setPurchaseID(Long purchaseID) {
        this.purchaseID = purchaseID;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setMatchID(Long matchID) {
        this.matchID = matchID;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setCollected(Boolean collected) {
        this.collected = collected;
    }

    @Override
    public String toString(){
        return "PurchaseDto [purchaseId=" + purchaseID + ", userEmail=" + userEmail
                + ", bankCard=" + bankCard + ", purchaseDate=" + purchaseDate
                + ", matchId = " + matchID + ", units=" + units
                + ", collected=" + collected + "]";
    }
}
