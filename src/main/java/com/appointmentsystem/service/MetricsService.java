package com.appointmentsystem.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MetricsService {

    private final AtomicInteger totalBookings = new AtomicInteger(0);
    private final AtomicInteger failedBookings = new AtomicInteger(0);
    private final CopyOnWriteArrayList<Long> bookingLatencies = new CopyOnWriteArrayList<>();
    private final LocalDateTime startTime = LocalDateTime.now();

    public void recordBookingSuccess(long latencyMs) {
        totalBookings.incrementAndGet();
        bookingLatencies.add(latencyMs);
        // Keep only last 100 latencies to avoid memory bloat
        if (bookingLatencies.size() > 100) {
            bookingLatencies.remove(0);
        }
    }

    public void recordBookingFailure() {
        failedBookings.incrementAndGet();
    }

    public int getTotalBookings() {
        return totalBookings.get();
    }

    public int getFailedBookings() {
        return failedBookings.get();
    }

    public double getAverageLatencyMs() {
        if (bookingLatencies.isEmpty()) {
            return 0.0;
        }
        return bookingLatencies.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0.0);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getUptimeSeconds() {
        return ChronoUnit.SECONDS.between(startTime, LocalDateTime.now());
    }
}
