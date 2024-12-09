import com.fasterxml.jackson.databind.ObjectMapper;
import enums.SystemConfigTypes;
import models.Customer;
import models.TicketPool;
import models.Vendor;
import models.fileOps.Config;
import util.GetInput;

import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        int totalTickets;
        int ticketReleaseRate;
        int customerRetrievalRate;
        int maxTicketCapacity;

        try {
            // Read JSON file
            File file = new File("../save_data_file.json");
            Config config = objectMapper.readValue(file, Config.class);

            // Update CLI status
            config.getSystemConfigs().setCliStatus(true);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);

            // Load current data
            totalTickets = config.getSystemConfigs().getTotalTickets();
            ticketReleaseRate = config.getSystemConfigs().getTicketReleaseRate();
            customerRetrievalRate = config.getSystemConfigs().getCustomerRetrievalRate();
            maxTicketCapacity = config.getSystemConfigs().getMaxTicketCapacity();

            // Update new configs
            updateSystemConfigs(SystemConfigTypes.TOTAL_TICKETS, config, totalTickets, maxTicketCapacity);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);

            updateSystemConfigs(SystemConfigTypes.TICKET_RELEASE_RATE, config, ticketReleaseRate, 0);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);

            updateSystemConfigs(SystemConfigTypes.CUSTOMER_RETRIEVAL_RATE, config, customerRetrievalRate, 0);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);

            updateSystemConfigs(SystemConfigTypes.MAX_TICKET_CAPACITY, config, maxTicketCapacity, 0);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);

            // Start system
            boolean loopBreak = false;
            while (!loopBreak) {
                String userInput = new GetInput().getStr("Do you need to start System (y/n): ");
                if (userInput.equalsIgnoreCase("y")) {
                    loopBreak = true;

                } else if (userInput.equalsIgnoreCase("n")) {
                    config.getSystemConfigs().setCliStatus(false);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);
                    System.exit(0);
                }
            }

            config.getSystemConfigs().setSystemStatus(true);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);

            TicketPool ticketPool = new TicketPool(objectMapper, file, config);
            Vendor vendor = new Vendor(ticketPool, config);
            Customer customer = new Customer(ticketPool, config);

            Thread vendorThread = new Thread(vendor);
            Thread customerThread = new Thread(customer);

            vendorThread.start();
            customerThread.start();

            // Add a shutdown hook to handle thread termination
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown initiated. Stopping threads...");
                vendor.stop(); // Signal the vendor thread to stop
                customer.stop(); // Signal the customer thread to stop

                try {
                    vendorThread.join(); // Wait for the thread to finish
                    customerThread.join(); // Wait for the thread to finish

                    // update system and CLI status
                    config.getSystemConfigs().setSystemStatus(false);
                    config.getSystemConfigs().setCliStatus(false);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);

                } catch (InterruptedException | IOException e) {
                    System.err.println("Error during shutdown: " + e.getMessage());
                }

                System.out.println("All threads have been stopped. Goodbye!");
            }));

            // Keep the main program running indefinitely
            while (true) {
                try {
                    Thread.sleep(1000); // Simulate the main program work
                } catch (InterruptedException e) {
                    System.out.println("Main thread interrupted: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error initializing system: " + e.getMessage());
        }

    }


    private static void updateSystemConfigs(SystemConfigTypes type, Config configFile, int value1, int value2) {
        GetInput getInput = new GetInput();

        // Java 17+ switch that directory return the value
        String config = switch (type) {
            case TOTAL_TICKETS -> "Total Tickets";
            case TICKET_RELEASE_RATE -> "Ticket Release Rate (ms)";
            case CUSTOMER_RETRIEVAL_RATE -> "Customer Retrieval Rate (ms)";
            case MAX_TICKET_CAPACITY -> "Max Ticket Capacity";
        };

        System.out.println(config + ": " + value1);
        while (true) {
            String userInput = getInput.getStr("Do you need to update " + config.toLowerCase() +" (y/n): ");
            if (userInput.equalsIgnoreCase("y")) {
                boolean loopBreak = false;

                while (!loopBreak) {
                    int newValue = getInput.getInt("Enter new " + config.toLowerCase() + " count: ");
                    if (newValue < 0) {
                        System.out.println();
                        continue;
                    }

                    switch (type) {
                        case TOTAL_TICKETS:
                            if (newValue <= value2) {
                                configFile.getSystemConfigs().setTotalTickets(newValue);
                                loopBreak = true;

                            } else {
                                System.out.println("Value should be less than max ticket capacity " + value2 + " !\n");
                            }
                            break;

                        case TICKET_RELEASE_RATE:
                            configFile.getSystemConfigs().setTicketReleaseRate(newValue);
                            loopBreak = true;
                            break;

                        case CUSTOMER_RETRIEVAL_RATE:
                            configFile.getSystemConfigs().setCustomerRetrievalRate(newValue);
                            loopBreak = true;
                            break;

                        case MAX_TICKET_CAPACITY:
                            configFile.getSystemConfigs().setMaxTicketCapacity(newValue);
                            loopBreak = true;
                            break;
                    }
                }
                break;

            } else if (userInput.equalsIgnoreCase("n")) {
                break;
            }
        }

        System.out.println();
    }
}
