package models;

import models.fileOps.Config;

import static java.lang.Thread.sleep;

public class Customer implements Runnable {
    private TicketPool ticketPool;
    private volatile boolean running = true; // Flag to control thread execution
    private Config config;


    public Customer(TicketPool ticketPool, Config config) {
        this.ticketPool = ticketPool;
        this.config = config;
    }

    public void stop() {
        running = false; // Signal to stop the thread
    }

    @Override
    public void run() {
        while (running) {
            ticketPool.removeTicket();

            try {
                sleep(config.getSystemConfigs().getCustomerRetrievalRate()); // Simulate work

            } catch (InterruptedException e) {
                System.out.println("Customer thread interrupted: " + e.getMessage());
            }
        }

        System.out.println("Customer thread is going to stop...");
    }
}
