package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/systemconfigs")
public class SystemConfigsController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("../save_data_file.json");

    public SystemConfigsController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Endpoint to fetch the current system_configs
    @GetMapping("/")
    public Map<String, Object> getSystemConfigs() throws Exception {
        Map<String, Object> data = objectMapper.readValue(file, Map.class);
        return (Map<String, Object>) data.get("system_configs");
    }

    // Broadcast system_configs updates via WebSocket
    @Scheduled(fixedRate = 1000) // Send updates every 1 seconds
    public void broadcastSystemConfigs() throws Exception {
        Map<String, Object> data = objectMapper.readValue(file, Map.class);
        Map<String, Object> systemConfigs = (Map<String, Object>) data.get("system_configs");

        if (systemConfigs != null) {
            messagingTemplate.convertAndSend("/topic/systemConfigs", systemConfigs);
        }
    }

    @PatchMapping("/update")
    public void updateSystemConfigs(@RequestBody Map<String, Object> updates) throws Exception {
        // Read the existing JSON file
        Map<String, Object> data = objectMapper.readValue(file, Map.class);

        // Get the current system_configs
        Map<String, Object> systemConfigs = (Map<String, Object>) data.get("system_configs");

        // Update only the allowed keys
        if (updates.containsKey("total_tickets")) {
            systemConfigs.put("total_tickets", updates.get("total_tickets"));
        }
        if (updates.containsKey("ticket_release_rate")) {
            systemConfigs.put("ticket_release_rate", updates.get("ticket_release_rate"));
        }
        if (updates.containsKey("customer_retrieval_rate")) {
            systemConfigs.put("customer_retrieval_rate", updates.get("customer_retrieval_rate"));
        }
        if (updates.containsKey("max_ticket_capacity")) {
            systemConfigs.put("max_ticket_capacity", updates.get("max_ticket_capacity"));
        }

        // Save the updated data back to the JSON file
        objectMapper.writeValue(file, data);
    }
}
