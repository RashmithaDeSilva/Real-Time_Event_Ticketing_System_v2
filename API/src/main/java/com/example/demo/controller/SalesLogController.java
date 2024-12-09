package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/saleslog")
public class SalesLogController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("../save_data_file.json");

    public SalesLogController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Fetch sales_logs from save_data_file.json
    @GetMapping("/")
    public List<Map<String, String>> getSalesLogs(@RequestParam int page, @RequestParam int size) throws Exception {
        Map<String, Object> data = objectMapper.readValue(file, Map.class);
        List<Map<String, String>> salesLogs = (List<Map<String, String>>) data.get("sales_logs");

        int start = Math.min(page * size, salesLogs.size());
        int end = Math.min(start + size, salesLogs.size());

        return salesLogs.subList(start, end);
    }

    // Broadcast sales_logs via WebSocket
    @Scheduled(fixedRate = 1000) // Send updates every 1 seconds
    public void broadcastSalesLogs() throws Exception {
        Map<String, Object> data = objectMapper.readValue(file, Map.class);
        List<Map<String, String>> salesLogs = (List<Map<String, String>>) data.get("sales_logs");

        if (!salesLogs.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/salesLogs", salesLogs);
        }
    }
}
