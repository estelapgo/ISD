package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.app.thrift.ThriftPurchaseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientPurchaseDtoToThriftPurchaseDtoConversor {

    public static ThriftPurchaseDto toThriftPurchaseDto(ClientPurchaseDto purchaseDto) {

        Long purchaseID = purchaseDto.getPurchaseId();

        return new ThriftPurchaseDto(
                purchaseID == null ? -1 : purchaseID.longValue(),
                purchaseDto.getUserEmail(),purchaseDto.getBankCard(),
                purchaseDto.getPurchaseDate().toString(),purchaseDto.getMatchId(),
                purchaseDto.getUnits(),purchaseDto.getCollected());
    }

    public static ClientPurchaseDto toClientPurchaseDto(ThriftPurchaseDto purchase) {
        return new ClientPurchaseDto(
                purchase.getPurchaseId(),
                purchase.getUserEmail(),
                purchase.getBankCard(),
                LocalDateTime.parse(purchase.getPurchaseDate()),
                purchase.getMatchId(),
                purchase.getUnits(),
                purchase.isCollected());
    }
}
