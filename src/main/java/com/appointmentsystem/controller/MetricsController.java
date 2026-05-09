package com.appointmentsystem.controller;

import com.appointmentsystem.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("total_bookings", metricsService.getTotalBookings());
        metrics.put("failed_bookings", metricsService.getFailedBookings());
        metrics.put("average_latency_ms", String.format("%.2f", metricsService.getAverageLatencyMs()));
        metrics.put("uptime_seconds", metricsService.getUptimeSeconds());
        metrics.put("server_start_time", metricsService.getStartTime().toString());
        metrics.put("current_time", LocalDateTime.now().toString());
        
        // Calculate success rate
        int total = metricsService.getTotalBookings();
        int failed = metricsService.getFailedBookings();
        double successRate = total > 0 ? ((double)(total - failed) / total * 100) : 0.0;
        metrics.put("success_rate_percent", String.format("%.2f", successRate));
        
        return ResponseEntity.ok(metrics);
    }
}
