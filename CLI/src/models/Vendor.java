package models;

import models.fileOps.Config;

import static java.lang.Thread.sleep;

public class Vendor implements Runnable {
    public Customer customer;
    private TicketPool ticketPool;
    private volatile boolean running = true; // Flag to control thread execution
    private Config config;

    public Vendor(TicketPool ticketPool, Config config) {
        this.ticketPool = ticketPool;
        this.config = config;
    }

    public void stop() {
        running = false; // Signal to stop the thread
    }

    @Override
    public void run() {
        while (running) {
            ticketPool.addTickets(1);

            try {
                sleep(config.getSystemConfigs().getTicketReleaseRate());

            } catch (InterruptedException e) {
                System.out.println("Vendor thread interrupted: " + e.getMessage());
            }
        }

        System.out.println("Vendor thread is going to stop...");
    }
}
