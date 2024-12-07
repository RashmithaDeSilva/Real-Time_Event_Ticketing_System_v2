import com.fasterxml.jackson.databind.ObjectMapper;
import models.fileOps.Config;
import models.fileOps.SalesLog;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read JSON file
            File file = new File("../save_data_file.json");
            Config config = objectMapper.readValue(file, Config.class);

            // Print the current data
            System.out.println("Total Tickets: " + config.getSystemConfigs().getTotalTickets());
            System.out.println("Logs: ");
            config.getSalesLogs().forEach(log -> System.out.println(log.getLog()));

            // Update data
            config.getSystemConfigs().setTotalTickets(200);
            config.getSalesLogs().add(new SalesLog("Added a new log ----------------------", "22:00:00 - 08/12/2024"));

            // Print updated data
            System.out.println("Updated Total Tickets: " + config.getSystemConfigs().getTotalTickets());
            System.out.println("Updated Logs: ");
            config.getSalesLogs().forEach(log -> System.out.println(log.getLog()));

            // Write updated data back to file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);
            System.out.println("Data successfully saved to the file!");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
