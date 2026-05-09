package com.appointmentsystem.service;

import com.appointmentsystem.model.AppointmentHistoryEntry;
import com.appointmentsystem.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class AppointmentManagementService {

    private static final DateTimeFormatter DB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DB_FORMAT_WITH_DASH = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
    private static final DateTimeFormatter DISPLAY_DATETIME = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
    private static final DateTimeFormatter DISPLAY_CREATED = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    private final AppointmentRepository appointmentRepository;

    public AppointmentManagementService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<AppointmentHistoryEntry> findHistory() {
        List<AppointmentHistoryEntry> appointments = appointmentRepository.findAllAppointments();
        for (AppointmentHistoryEntry appointment : appointments) {
            appointment.setDisplayStartTime(formatDateTime(appointment.getStartTime(), DISPLAY_DATETIME));
            appointment.setDisplayEndTime(formatDateTime(appointment.getEndTime(), DISPLAY_DATETIME));
            appointment.setDisplayCreatedAt(formatCreatedAt(appointment.getCreatedAt()));
        }
        return appointments;
    }

    @Transactional
    public void cancelAppointment(long appointmentId) {
        long slotId = appointmentRepository.findSlotIdByAppointmentId(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment " + appointmentId + " not found"));
        appointmentRepository.deleteAppointment(appointmentId);
        appointmentRepository.reopenSlot(slotId);
    }

    private String formatDateTime(String value, DateTimeFormatter outputFormatter) {
        if (value == null || value.isBlank()) {
            return "";
        }
        try {
            LocalDateTime dateTime = parseDateTime(value, DB_FORMAT, DB_FORMAT_WITH_DASH);
            return dateTime.format(outputFormatter);
        } catch (IllegalArgumentException ex) {
            return value;
        }
    }

    private String formatCreatedAt(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        try {
            LocalDateTime dateTime = parseDateTime(
                    value,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            return dateTime.format(DISPLAY_CREATED);
        } catch (IllegalArgumentException ex) {
            return value;
        }
    }

    private LocalDateTime parseDateTime(String value, DateTimeFormatter... formatters) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // Try the next known storage format.
            }
        }
        throw new IllegalArgumentException("Unsupported date/time value: " + value);
    }
}