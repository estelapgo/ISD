package es.udc.ws.app.restservice.dto;


import es.udc.ws.app.model.util.Purchase.Purchase;

import java.util.ArrayList;
import java.util.List;

public class PurchaseToRestPurchaseDtoConversor {

    public static List<RestPurchaseDto> toRestPurchaseDto(List<Purchase> purchases) {
        List<RestPurchaseDto> purchaseDtos = new ArrayList<>(purchases.size());

        for (int i = 0; i < purchases.size(); i++){
            Purchase purchase = purchases.get(i);
            purchaseDtos.add(toRestPurchaseDto(purchase));
        }

        return purchaseDtos;
    }

    public static RestPurchaseDto toRestPurchaseDto(Purchase purchase){
        String lastCreditCardNumbers = purchase.getBankCard().substring(purchase.getBankCard().length() - 4);
        String hiddenCreditCard = "*".repeat(Math.max(0, purchase.getBankCard().length() - 4)) + lastCreditCardNumbers;

        return new RestPurchaseDto(purchase.getPurchaseID(),purchase.getUserEmail(),
                hiddenCreditCard,purchase.getPurchaseDate(),purchase.getMatchID(),
                purchase.getUnits(),purchase.getCollected());
    }

    public static Purchase toPurchase(RestPurchaseDto purchase) {
        return new Purchase(purchase.getPurchaseID(), purchase.getUserEmail(),
                purchase.getBankCard(),purchase.getPurchaseDate(),purchase.getMatchID(),
                purchase.getUnits(),purchase.getCollected());
    }
}
