package com.appointmentsystem.controller;

import com.appointmentsystem.dto.BookingRequest;
import com.appointmentsystem.exception.SlotUnavailableException;
import com.appointmentsystem.service.BookingService;
import com.appointmentsystem.service.MetricsService;
import com.appointmentsystem.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final NotificationService notificationService;
    private final MetricsService metricsService;

    public BookingController(BookingService bookingService, NotificationService notificationService, MetricsService metricsService) {
        this.bookingService      = bookingService;
        this.notificationService = notificationService;
        this.metricsService      = metricsService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<String> bookAppointment(@RequestBody BookingRequest request) {
        try {
            bookingService.bookAppointment(
                request.getSlotId(),
                request.getStudentId(),
                request.getProviderId(),
                request.getServiceId(),
                request.getSubject()
            );
            try {
                // Notification is best-effort: failures should not make the booking fail
                notificationService.sendConfirmation(request.getStudentId(), request.getSlotId());
            } catch (Exception ex) {
                log.error("Notification failed (non-fatal): student={} slot={}", request.getStudentId(), request.getSlotId(), ex);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Appointment booked successfully");
        } catch (IllegalArgumentException e) {
            metricsService.recordBookingFailure();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SlotUnavailableException e) {
            metricsService.recordBookingFailure();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
