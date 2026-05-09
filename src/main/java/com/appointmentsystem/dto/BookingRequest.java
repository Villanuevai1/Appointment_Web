package com.appointmentsystem.dto;

public class BookingRequest {

    private long slotId;
    private long studentId;
    private long providerId;
    private long serviceId;
    private String subject;

    public BookingRequest() {}

    public BookingRequest(long slotId, long studentId, long providerId, long serviceId) {
        this.slotId = slotId;
        this.studentId = studentId;
        this.providerId = providerId;
        this.serviceId = serviceId;
    }

    public BookingRequest(long slotId, long studentId, long providerId, long serviceId, String subject) {
        this.slotId = slotId;
        this.studentId = studentId;
        this.providerId = providerId;
        this.serviceId = serviceId;
        this.subject = subject;
    }

    public long getSlotId()     { return slotId; }
    public long getStudentId()  { return studentId; }
    public long getProviderId() { return providerId; }
    public long getServiceId()  { return serviceId; }
    public String getSubject()  { return subject; }

    public void setSlotId(long slotId)         { this.slotId = slotId; }
    public void setStudentId(long studentId)   { this.studentId = studentId; }
    public void setProviderId(long providerId) { this.providerId = providerId; }
    public void setServiceId(long serviceId)   { this.serviceId = serviceId; }
    public void setSubject(String subject)     { this.subject = subject; }
}
