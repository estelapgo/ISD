package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientMatchDto {
    private Long matchId;
    private LocalDateTime matchDate;
    private double ticketsPrice;
    private int capacity;
    private int soldUnits;
    private String visitingTeam;


    public ClientMatchDto(Long matchId, LocalDateTime matchDate, double ticketsPrice, int capacity,String visitingTeam) {
        this.matchId = matchId;
        this.matchDate = matchDate;
        this.ticketsPrice = ticketsPrice;
        this.capacity = capacity;
        this.soldUnits = 0;
        this.visitingTeam = visitingTeam;
    }

    public ClientMatchDto(Long matchId, LocalDateTime matchDate, double ticketsPrice, int capacity,int soldUnits,String visitingTeam) {
        this.matchId = matchId;
        this.matchDate = matchDate;
        this.ticketsPrice = ticketsPrice;
        this.capacity = capacity;
        this.soldUnits = soldUnits;
        this.visitingTeam = visitingTeam;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
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

    public void setSoldUnits(int soldUnits) {
        this.soldUnits = soldUnits;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    @Override
    public String toString() {
        return "MatchDto [matchId=" + matchId + ", matchDate=" + matchDate
                + ", ticketsPrice=" + ticketsPrice + ", capacity=" + capacity
                + ", soldUnits=" + soldUnits + ", visitingTeam=" + visitingTeam + "]";
    }
}
