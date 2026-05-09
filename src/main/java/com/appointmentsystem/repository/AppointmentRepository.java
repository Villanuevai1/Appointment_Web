package com.appointmentsystem.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.appointmentsystem.model.AvailabilitySlot;
import com.appointmentsystem.model.AppointmentHistoryEntry;

import java.util.Optional;
import java.util.List;

@Repository
public class AppointmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AvailabilitySlot> findAllSlots() {
        return jdbcTemplate.query(
            "SELECT id, provider_id, subject, start_time, end_time, status FROM availability_slots ORDER BY start_time",
            (rs, rowNum) -> new AvailabilitySlot(
                rs.getLong("id"),
                rs.getLong("provider_id"),
                rs.getString("subject"),
                rs.getString("start_time"),
                rs.getString("end_time"),
                rs.getString("status")
            )
        );
    }

    public void insertSlot(long providerId, String subject, String startTime, String endTime) {
        jdbcTemplate.update(
            "INSERT INTO availability_slots (provider_id, subject, start_time, end_time, status) VALUES (?, ?, ?, ?, 'OPEN')",
            providerId, subject, startTime, endTime
        );
    }

    /**
     * Atomically claims an OPEN slot by updating its status to BOOKED.
     * The WHERE status = 'OPEN' guard is what prevents double-booking:
     * only the first concurrent caller will match the row and get rowsAffected = 1.
     * Every subsequent caller will find status = 'BOOKED' and get rowsAffected = 0.
     *
     * @return 1 if the slot was successfully claimed, 0 if already taken
     */
    public int claimSlot(long slotId) {
        return jdbcTemplate.update(
            "UPDATE availability_slots SET status = 'BOOKED' WHERE id = ? AND status = 'OPEN'",
            slotId
        );
    }

    public void insertAppointment(long slotId, long studentId, long providerId, long serviceId, String subject) {
        jdbcTemplate.update(
            "INSERT INTO appointments (slot_id, student_id, provider_id, service_id, subject, created_at) " +
            "VALUES (?, ?, ?, ?, ?, datetime('now'))",
            slotId, studentId, providerId, serviceId, subject
        );
    }

    public List<AppointmentHistoryEntry> findAllAppointments() {
        return jdbcTemplate.query(
            "SELECT a.id AS appointment_id, a.slot_id, a.student_id, a.provider_id, a.service_id, a.subject, a.created_at, " +
            "s.start_time, s.end_time " +
            "FROM appointments a JOIN availability_slots s ON a.slot_id = s.id " +
            "ORDER BY a.created_at DESC",
            (rs, rowNum) -> new AppointmentHistoryEntry(
                rs.getLong("appointment_id"),
                rs.getLong("slot_id"),
                rs.getLong("student_id"),
                rs.getLong("provider_id"),
                rs.getLong("service_id"),
                rs.getString("subject"),
                rs.getString("start_time"),
                rs.getString("end_time"),
                rs.getString("created_at")
            )
        );
    }

    public Optional<Long> findSlotIdByAppointmentId(long appointmentId) {
        List<Long> slotIds = jdbcTemplate.query(
            "SELECT slot_id FROM appointments WHERE id = ?",
            (rs, rowNum) -> rs.getLong("slot_id"),
            appointmentId
        );
        return slotIds.stream().findFirst();
    }

    public void deleteAppointment(long appointmentId) {
        jdbcTemplate.update("DELETE FROM appointments WHERE id = ?", appointmentId);
    }

    public void reopenSlot(long slotId) {
        jdbcTemplate.update("UPDATE availability_slots SET status = 'OPEN' WHERE id = ?", slotId);
    }
}
