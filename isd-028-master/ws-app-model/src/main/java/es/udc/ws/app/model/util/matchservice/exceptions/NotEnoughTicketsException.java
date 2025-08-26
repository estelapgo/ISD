package es.udc.ws.app.model.util.matchservice.exceptions;

public class NotEnoughTicketsException extends Exception{

        public Long matchID;
        public int ticketsRequested;
        public int ticketsAvailable;

        public NotEnoughTicketsException(Long matchID, int ticketsRequested, int ticketsAvailable){
            super("Not enough tickets for match with ID = " + matchID + ". Requested = " + ticketsRequested + ", available = " + ticketsAvailable);
            this.matchID = matchID;
            this.ticketsRequested = ticketsRequested;
            this.ticketsAvailable = ticketsAvailable;
        }

        public Long getMatchID() {
            return matchID;
        }

        public void setMatchID(Long matchID) {
            this.matchID = matchID;
        }

        public int getTicketsRequested() {
            return ticketsRequested;
        }

        public void setTicketsRequested(int ticketsRequested) {
            this.ticketsRequested = ticketsRequested;
        }

        public int getTicketsAvailable() {
            return ticketsAvailable;
        }

        public void setTicketsAvailable(int ticketsAvailable) {
            this.ticketsAvailable = ticketsAvailable;
        }
}
