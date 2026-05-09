package com.appointmentsystem.service;

import com.appointmentsystem.exception.SlotUnavailableException;
import com.appointmentsystem.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final AppointmentRepository appointmentRepository;
    private final MetricsService metricsService;

    public BookingService(AppointmentRepository appointmentRepository, MetricsService metricsService) {
        this.appointmentRepository = appointmentRepository;
        this.metricsService = metricsService;
    }

    /**
     * Books an appointment for a student against an availability slot.
     *
     * Concurrency safety:
     *   1. claimSlot() fires a single atomic UPDATE ... WHERE status = 'OPEN'.
     *      Only one concurrent caller can win — the DB serializes conflicting writes.
     *   2. @Transactional ensures the UPDATE + INSERT are committed together or
     *      both rolled back on any error (e.g. the UNIQUE(slot_id) safety-net fires).
     *   3. No SELECT-then-UPDATE pattern is used, eliminating the TOCTOU race condition.
     */
    @Transactional(rollbackFor = Exception.class)
    public void bookAppointment(long slotId, long studentId, long providerId, long serviceId, String subject) {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject is required");
        }
        long startTime = System.currentTimeMillis();
        log.info("Booking attempt: student={} slot={} subject={}", studentId, slotId, subject);
        int rowsAffected = appointmentRepository.claimSlot(slotId);

        if (rowsAffected == 0) {
            log.warn("Slot {} unavailable", slotId);
            throw new SlotUnavailableException(slotId);
        }

        appointmentRepository.insertAppointment(slotId, studentId, providerId, serviceId, subject.trim());
        long latencyMs = System.currentTimeMillis() - startTime;
        metricsService.recordBookingSuccess(latencyMs);
        log.info("Slot {} claimed successfully for student {} subject={} ({}ms)", slotId, studentId, subject, latencyMs);
    }
}
