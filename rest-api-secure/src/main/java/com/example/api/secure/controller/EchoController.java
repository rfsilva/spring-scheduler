package com.example.api.secure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/echo")
public class EchoController {

    @GetMapping("/{message}")
    public ResponseEntity<Map<String, String>> echo(@PathVariable String message) {
        Map<String, String> response = new HashMap<>();
        response.put("echo", message);
        response.put("service", "Secure REST API");
        return ResponseEntity.ok(response);
    }
}