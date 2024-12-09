package models.fileOps;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SystemConfigs {
    @JsonProperty("total_tickets")
    private int totalTickets;

    @JsonProperty("ticket_release_rate")
    private int ticketReleaseRate;

    @JsonProperty("customer_retrieval_rate")
    private int customerRetrievalRate;

    @JsonProperty("max_ticket_capacity")
    private int maxTicketCapacity;

    @JsonProperty("system_status")
    private boolean systemStatus;

    @JsonProperty("cli_status")
    private boolean cliStatus;


    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public boolean isCliStatus() {
        return cliStatus;
    }

    public void setCliStatus(boolean cliStatus) {
        this.cliStatus = cliStatus;
    }

    public boolean isSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(boolean systemStatus) {
        this.systemStatus = systemStatus;
    }

    public void addTickets(int count) {
        this.totalTickets += count;
    }

    public void removeTickets(int count) {
        this.totalTickets -= count;
    }
}
