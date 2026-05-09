package com.appointmentsystem.model;

public class AppointmentHistoryEntry {
    private long appointmentId;
    private long slotId;
    private long studentId;
    private long providerId;
    private long serviceId;
    private String subject;
    private String startTime;
    private String endTime;
    private String createdAt;
    private String displayStartTime;
    private String displayEndTime;
    private String displayCreatedAt;

    public AppointmentHistoryEntry() {}

    public AppointmentHistoryEntry(long appointmentId, long slotId, long studentId, long providerId, long serviceId,
                                   String subject, String startTime, String endTime, String createdAt) {
        this.appointmentId = appointmentId;
        this.slotId = slotId;
        this.studentId = studentId;
        this.providerId = providerId;
        this.serviceId = serviceId;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
    }

    public long getAppointmentId() { return appointmentId; }
    public long getSlotId() { return slotId; }
    public long getStudentId() { return studentId; }
    public long getProviderId() { return providerId; }
    public long getServiceId() { return serviceId; }
    public String getSubject() { return subject; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getCreatedAt() { return createdAt; }
    public String getDisplayStartTime() { return displayStartTime; }
    public String getDisplayEndTime() { return displayEndTime; }
    public String getDisplayCreatedAt() { return displayCreatedAt; }

    public void setAppointmentId(long appointmentId) { this.appointmentId = appointmentId; }
    public void setSlotId(long slotId) { this.slotId = slotId; }
    public void setStudentId(long studentId) { this.studentId = studentId; }
    public void setProviderId(long providerId) { this.providerId = providerId; }
    public void setServiceId(long serviceId) { this.serviceId = serviceId; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setDisplayStartTime(String displayStartTime) { this.displayStartTime = displayStartTime; }
    public void setDisplayEndTime(String displayEndTime) { this.displayEndTime = displayEndTime; }
    public void setDisplayCreatedAt(String displayCreatedAt) { this.displayCreatedAt = displayCreatedAt; }
}
