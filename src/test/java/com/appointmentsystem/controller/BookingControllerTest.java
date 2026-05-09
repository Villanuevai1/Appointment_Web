package com.appointmentsystem.controller;

import com.appointmentsystem.dto.BookingRequest;
import com.appointmentsystem.dto.NotificationResponse;
import com.appointmentsystem.exception.SlotUnavailableException;
import com.appointmentsystem.service.BookingService;
import com.appointmentsystem.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void bookAppointmentReturnsCreatedWhenBookingSucceeds() {
        BookingRequest request = new BookingRequest(21L, 3L, 4L, 5L);
        when(notificationService.sendConfirmation(3L, 21L))
                .thenReturn(new NotificationResponse("SENT", "Confirmation sent for slot 21"));

        ResponseEntity<String> response = bookingController.bookAppointment(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Appointment booked successfully", response.getBody());
        verify(bookingService).bookAppointment(21L, 3L, 4L, 5L);
        verify(notificationService).sendConfirmation(3L, 21L);
    }

    @Test
    void bookAppointmentReturnsConflictWhenSlotIsUnavailable() {
        BookingRequest request = new BookingRequest(21L, 3L, 4L, 5L);
        doThrow(new SlotUnavailableException(21L))
                .when(bookingService)
                .bookAppointment(21L, 3L, 4L, 5L);

        ResponseEntity<String> response = bookingController.bookAppointment(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Slot 21 is no longer available", response.getBody());
        verify(bookingService).bookAppointment(21L, 3L, 4L, 5L);
        verifyNoInteractions(notificationService);
    }
}