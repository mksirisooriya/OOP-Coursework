package com.ticketing.system.dto;

public class TicketStatus {
    private long availableTickets;
    private long totalTickets;
    private long remainingTickets;
    private long soldTickets;

    public TicketStatus(long availableTickets, long totalTickets, long remainingTickets, long soldTickets) {
        this.availableTickets = availableTickets;
        this.totalTickets = totalTickets;
        this.remainingTickets = remainingTickets;
        this.soldTickets = soldTickets;
    }

    public long getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(long availableTickets) {
        this.availableTickets = availableTickets;
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(long totalTickets) {
        this.totalTickets = totalTickets;
    }

    public long getRemainingTickets() {
        return remainingTickets;
    }

    public void setRemainingTickets(long remainingTickets) {
        this.remainingTickets = remainingTickets;
    }

    public long getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(long soldTickets) {
        this.soldTickets = soldTickets;
    }
}