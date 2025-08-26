package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.util.matchservice.exceptions.AlreadyCollectedException;
import es.udc.ws.app.model.util.matchservice.exceptions.IncorrectBankCardException;
import es.udc.ws.app.model.util.matchservice.exceptions.MatchNotAvailableException;
import es.udc.ws.app.model.util.matchservice.exceptions.NotEnoughTicketsException;

public class AppExceptionToJsonConversor {

    public static ObjectNode toAlreadyCollectedException(AlreadyCollectedException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","AlreadyCollected");
        exceptionObject.put("purchaseId",(ex.getPurchaseID() != null ? ex.getPurchaseID():null));

        return exceptionObject;
    }

    public static ObjectNode toIncorrectBankCardException(IncorrectBankCardException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","IncorrectBankCard");
        exceptionObject.put("purchaseId",(ex.getPurchaseID() != null ? ex.getPurchaseID():null));

        return exceptionObject;
    }

    public static ObjectNode toMatchNotAvaliableException(MatchNotAvailableException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","MatchNotAvaliable");
        exceptionObject.put("matchDate",(ex.getDate().toString() != null ? ex.getDate().toString():null));

        return exceptionObject;
    }

    public static ObjectNode toNotEnoughTicketsException(NotEnoughTicketsException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","NotEnoughTickets");
        exceptionObject.put("matchId",(ex.getMatchID() != null ? ex.getMatchID():null));
        exceptionObject.put("avaliableTickets",ex.getTicketsAvailable());
        exceptionObject.put("requestedTickets",ex.getTicketsRequested());

        return exceptionObject;
    }
}
