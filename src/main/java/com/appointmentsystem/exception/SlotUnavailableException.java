package com.appointmentsystem.exception;

public class SlotUnavailableException extends RuntimeException {

    public SlotUnavailableException(long slotId) {
        super("Slot " + slotId + " is no longer available");
    }
}
