package com.appointmentsystem.model;

public class AvailabilitySlot {
    private long id;
    private long providerId;
    private String subject;
    private String startTime;
    private String endTime;
    private String status;

    public AvailabilitySlot() {}

    public AvailabilitySlot(long id, long providerId, String subject, String startTime, String endTime, String status) {
        this.id = id;
        this.providerId = providerId;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public long getId() { return id; }
    public long getProviderId() { return providerId; }
    public String getSubject() { return subject; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }

    public void setId(long id) { this.id = id; }
    public void setProviderId(long providerId) { this.providerId = providerId; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setStatus(String status) { this.status = status; }
}
