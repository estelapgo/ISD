package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientPurchaseDto {
    private Long purchaseId;
    private String userEmail;
    private String creditCardNumber;
    private LocalDateTime purchaseDate;
    private Long matchId;
    private int units;
    private Boolean collected;


    public ClientPurchaseDto(Long purchaseId, String userEmail, String creditCardNumber, LocalDateTime purchaseDate, Long matchId, int units, Boolean collected) {
        this.purchaseId = purchaseId;
        this.userEmail = userEmail;
        this.creditCardNumber = creditCardNumber;
        this.purchaseDate = purchaseDate;
        this.matchId = matchId;
        this.units = units;
        this.collected = collected;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBankCard() {
        return creditCardNumber;
    }

    public void setBankCard(String bankCard) {
        this.creditCardNumber = creditCardNumber;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
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
    public String toString(){
        return "PurchaseDto [purchaseId=" + purchaseId + ", userEmail=" + userEmail
                + ", bankCard=" + creditCardNumber + ", purchaseDate=" + purchaseDate
                + ", matchId = " + matchId + ", units=" + units
                + ", collected=" + collected + "]";
    }

}
