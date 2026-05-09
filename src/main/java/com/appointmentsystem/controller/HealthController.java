package com.appointmentsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;

    public HealthController(JdbcTemplate jdbcTemplate, RestTemplate restTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new LinkedHashMap<>();

        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            status.put("database", "UP");
        } catch (Exception e) {
            status.put("database", "DOWN");
        }

        try {
            restTemplate.getForObject("http://localhost:8080/mock/health", String.class);
            status.put("notification_service", "UP");
        } catch (Exception e) {
            status.put("notification_service", "DOWN");
        }

        boolean allUp = status.values().stream().allMatch(v -> v.equals("UP"));
        status.put("status", allUp ? "UP" : "DOWN");
        status.put("timestamp", Instant.now().toString());

        return allUp
            ? ResponseEntity.ok(status)
            : ResponseEntity.status(503).body(status);
    }
}