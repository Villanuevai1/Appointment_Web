package com.appointmentsystem.dto;

public class NotificationRequest {

    private long studentId;
    private long slotId;
    private String message;

    public NotificationRequest() {}

    public NotificationRequest(long studentId, long slotId, String message) {
        this.studentId = studentId;
        this.slotId    = slotId;
        this.message   = message;
    }

    public long   getStudentId() { return studentId; }
    public long   getSlotId()    { return slotId; }
    public String getMessage()   { return message; }

    public void setStudentId(long studentId)   { this.studentId = studentId; }
    public void setSlotId(long slotId)         { this.slotId = slotId; }
    public void setMessage(String message)     { this.message = message; }
}
