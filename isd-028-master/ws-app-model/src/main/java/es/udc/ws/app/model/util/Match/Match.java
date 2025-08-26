package es.udc.ws.app.model.util.Match;

import java.time.LocalDateTime;
import java.util.Objects;

public class Match {
    private Long matchID;
    private LocalDateTime matchDate;
    private double ticketsPrice;
    private int capacity;              //aforo-> Num max de entradas. Se define cuando se crea un partido y no cambia.
    private int soldUnits;                 //entradas vendidas. Va cambiando según se hagan reservas.
    private LocalDateTime registerDate;
    private String visitingTeam;

    public Match (LocalDateTime matchDate, double ticketsPrice, int capacity, String visitingTeam){
        this.matchDate = matchDate;
        this.ticketsPrice = ticketsPrice;
        this.capacity = capacity;
        this.soldUnits = 0;
        this.visitingTeam = visitingTeam;
    }

    public Match (Long matchID, LocalDateTime matchDate, double ticketsPrice, int capacity, String visitingTeam){
        this(matchDate, ticketsPrice, capacity, visitingTeam);
        this.matchID = matchID;
    }

    public Match (Long matchID, LocalDateTime matchDate, double ticketsPrice, int capacity, int soldUnits,
                  String visitingTeam){
        this(matchID, matchDate, ticketsPrice, capacity, visitingTeam);
        this.soldUnits = soldUnits;
    }

    public Match(Long matchID, LocalDateTime matchDate, double ticketsPrice, int capacity, int soldUnits,
                 LocalDateTime registerDate, String visitingTeam){
        this(matchID, matchDate, ticketsPrice, capacity, visitingTeam);
        this.soldUnits = soldUnits;
        this.registerDate = (registerDate != null) ? registerDate.withNano(0) : null;
    }

    //Setters y getters

    public Long getMatchID() {
        return matchID;
    }

    public void setMatchID(Long matchID) {
        this.matchID = matchID;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public double getTicketsPrice() {
        return ticketsPrice;
    }

    public void setTicketsPrice(double ticketsPrice) {
        this.ticketsPrice = ticketsPrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSoldUnits() {
        return soldUnits;
    }

    public void setSoldUnits(int units) {
        this.soldUnits = units;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    //equals y hashcode
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Match match = (Match) obj;
        return matchID.equals(match.matchID) && matchDate.equals(match.matchDate) && ticketsPrice == match.ticketsPrice
                && capacity == match.capacity && soldUnits == match.soldUnits && registerDate.equals(match.registerDate)
                && visitingTeam.equals(match.visitingTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchID, matchDate, ticketsPrice, capacity, soldUnits, registerDate, visitingTeam);
    }

}

