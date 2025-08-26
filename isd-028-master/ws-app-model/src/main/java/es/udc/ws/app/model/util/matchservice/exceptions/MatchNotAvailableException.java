package es.udc.ws.app.model.util.matchservice.exceptions;

import java.time.LocalDateTime;

public class MatchNotAvailableException extends Exception {

   public LocalDateTime date;
   public MatchNotAvailableException(LocalDateTime date){
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
