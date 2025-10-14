package com.example.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatusController {

    private final LocalDateTime startTime = LocalDateTime.now();

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("uptime", System.currentTimeMillis());
        
        Map<String, Object> memory = new HashMap<>();
        memory.put("total", Runtime.getRuntime().totalMemory());
        memory.put("free", Runtime.getRuntime().freeMemory());
        memory.put("max", Runtime.getRuntime().maxMemory());
        
        status.put("memory", memory);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("startTime", startTime);
        health.put("currentTime", LocalDateTime.now());
        return ResponseEntity.ok(health);
    }
}