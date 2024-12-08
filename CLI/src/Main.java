import com.fasterxml.jackson.databind.ObjectMapper;
import enums.SystemConfigTypes;
import models.fileOps.Config;
import models.fileOps.SalesLog;
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
        int systemStatus;
        int cliStatus;

        try {
            // Read JSON file
            File file = new File("../save_data_file.json");
            Config config = objectMapper.readValue(file, Config.class);

            // Update CLI status
            config.getSystemConfigs().setSystemStatus(true);
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
            

            // Write updated data back to file
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);
//            System.out.println("Data successfully saved to the file!");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    private static void updateSystemConfigs(SystemConfigTypes type, Config configFile, int value1, int value2) {
        GetInput getInput = new GetInput();

        // Java 17+ switch that directory return the value
        String config = switch (type) {
            case TOTAL_TICKETS -> "Total Tickets";
            case TICKET_RELEASE_RATE -> "Ticket Release Rate";
            case CUSTOMER_RETRIEVAL_RATE -> "Customer Retrieval Rate";
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
