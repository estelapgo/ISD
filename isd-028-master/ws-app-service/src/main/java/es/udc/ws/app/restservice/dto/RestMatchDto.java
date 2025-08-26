package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestMatchDto {
    private Long matchID;
    private LocalDateTime matchDate;
    private double ticketsPrice;
    private int capacity;
    private int soldUnits;
    private String visitingTeam;

    public RestMatchDto(Long matchID, LocalDateTime matchDate, double ticketsPrice, int capacity,String visitingTeam) {
        this.matchID = matchID;
        this.matchDate = matchDate;
        this.ticketsPrice = ticketsPrice;
        this.capacity = capacity;
        this.soldUnits = 0;
        this.visitingTeam = visitingTeam;
    }

    public RestMatchDto(Long matchID, LocalDateTime matchDate, double ticketsPrice, int capacity,int soldUnits,String visitingTeam) {
        this.matchID = matchID;
        this.matchDate = matchDate;
        this.ticketsPrice = ticketsPrice;
        this.capacity = capacity;
        this.soldUnits = soldUnits;
        this.visitingTeam = visitingTeam;
    }

    public Long getMatchID() {
        return matchID;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public double getTicketsPrice() {
        return ticketsPrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSoldUnits() {
        return soldUnits;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public void setMatchID(Long matchID) {
        this.matchID = matchID;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public void setTicketsPrice(double ticketsPrice) {
        this.ticketsPrice = ticketsPrice;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setSoldUnits(int soldUnits) {
        this.soldUnits = soldUnits;
    }

    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    @Override
    public String toString(){
        return "MatchDto [matchId=" + matchID + ", matchDate=" + matchDate
                + ", ticketsPrice=" + ticketsPrice + ", capacity=" + capacity
                + ", soldUnits=" + soldUnits + ", visitingTeam=" + visitingTeam + "]";
    }
}
