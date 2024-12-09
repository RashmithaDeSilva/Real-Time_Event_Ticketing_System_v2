package models;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.fileOps.Config;
import models.fileOps.SalesLog;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;


public class TicketPool {
    private static int totalTickets;
    private static int maxCapacity;
    private final ReentrantLock lock = new ReentrantLock();
    private ObjectMapper mapper;
    private Config configFile;
    private File file;

    public TicketPool(ObjectMapper objectMapper, File file, Config configFile) {
        this.mapper = objectMapper;
        this.file = file;
        this.configFile = configFile;
        totalTickets = configFile.getSystemConfigs().getTotalTickets();
        maxCapacity = configFile.getSystemConfigs().getMaxTicketCapacity();
    }

    // Synchronized method to add tickets
    public void addTickets(int count) {
        lock.lock();
        refreshPrams();
        if (totalTickets + count <= maxCapacity) {
            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();

            // Define the desired format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");

            try {
                configFile.getSystemConfigs().addTickets(count);
                configFile.getSalesLogs().add(new SalesLog("Vendor add " + count + " ticket into ticket pool", now.format(formatter)));
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, configFile);
                System.out.println("[" + now.format(formatter) + "] " + "Vendor add " + count + " ticket into ticket pool");
                totalTickets += count;

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        lock.unlock();
    }

    // Synchronized method to remove a ticket
    public void removeTicket() {
        lock.lock();
        refreshPrams();
        if (totalTickets > 0) {
            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();

            // Define the desired format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");

            try {
                configFile.getSystemConfigs().removeTickets(1);
                configFile.getSalesLogs().add(new SalesLog("Customer bought ticket from ticket pool", now.format(formatter)));
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, configFile);
                System.out.println("[" + now.format(formatter) + "] " + "Customer bought ticket from ticket pool");
                totalTickets -= 1;

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        lock.unlock();
    }

    private void refreshPrams() {
        totalTickets = configFile.getSystemConfigs().getTotalTickets();
        maxCapacity = configFile.getSystemConfigs().getMaxTicketCapacity();
    }
}
