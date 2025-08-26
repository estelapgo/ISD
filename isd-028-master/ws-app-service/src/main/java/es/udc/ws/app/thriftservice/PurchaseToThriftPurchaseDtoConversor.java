package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.util.Purchase.Purchase;
import es.udc.ws.app.thrift.ThriftPurchaseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PurchaseToThriftPurchaseDtoConversor {

    public static Purchase toPurchase(ThriftPurchaseDto purchase) {
        return new Purchase(purchase.getPurchaseId(),purchase.getUserEmail(),
                purchase.getBankCard(), LocalDateTime.parse(purchase.getPurchaseDate()),
                purchase.getMatchId(),purchase.getUnits(),purchase.isCollected());
    }

    public static List<ThriftPurchaseDto> toThriftPurchaseDtos(List<Purchase> purchases) {

        List<ThriftPurchaseDto> dtos = new ArrayList<>(purchases.size());

        for(Purchase purchase: purchases) {
            dtos.add(toThriftPurchaseDto(purchase));
        }

        return dtos;
    }

    public static ThriftPurchaseDto toThriftPurchaseDto(Purchase purchase) {

        return new ThriftPurchaseDto(purchase.getPurchaseID(),purchase.getUserEmail(),
                purchase.getBankCard(),purchase.getPurchaseDate().toString(),purchase.getMatchID(),
                purchase.getUnits(),purchase.getCollected());
    }
}
