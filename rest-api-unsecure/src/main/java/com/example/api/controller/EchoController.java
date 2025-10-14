package com.example.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/echo")
public class EchoController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> echoGet(@RequestParam(required = false) Map<String, String> params) {
        Map<String, Object> response = new HashMap<>();
        response.put("method", "GET");
        response.put("timestamp", System.currentTimeMillis());
        response.put("params", params);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> echoPost(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        response.put("method", "POST");
        response.put("timestamp", System.currentTimeMillis());
        response.put("body", body);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> echoPut(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        response.put("method", "PUT");
        response.put("timestamp", System.currentTimeMillis());
        response.put("body", body);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> echoDelete() {
        Map<String, Object> response = new HashMap<>();
        response.put("method", "DELETE");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}