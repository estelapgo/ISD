package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientMatchNotAvailableException extends Exception {

    public LocalDateTime date;
    public ClientMatchNotAvailableException(LocalDateTime date){
        super("Match not available for date = " + date + ".Because it already started");
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
