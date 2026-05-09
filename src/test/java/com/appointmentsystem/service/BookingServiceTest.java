package com.appointmentsystem.service;

import com.appointmentsystem.exception.SlotUnavailableException;
import com.appointmentsystem.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void bookAppointment_claimsSlotAndCreatesAppointmentWhenAvailable() {
        when(appointmentRepository.claimSlot(42L)).thenReturn(1);

        bookingService.bookAppointment(42L, 7L, 8L, 9L);

        verify(appointmentRepository).claimSlot(42L);
        verify(appointmentRepository).insertAppointment(42L, 7L, 8L, 9L);
        verifyNoMoreInteractions(appointmentRepository);
    }

    @Test
    void bookAppointment_throwsWhenSlotHasAlreadyBeenBooked() {
        when(appointmentRepository.claimSlot(42L)).thenReturn(0);

        SlotUnavailableException exception = assertThrows(
                SlotUnavailableException.class,
                () -> bookingService.bookAppointment(42L, 7L, 8L, 9L));

        assertEquals("Slot 42 is no longer available", exception.getMessage());
        verify(appointmentRepository).claimSlot(42L);
        verifyNoMoreInteractions(appointmentRepository);
    }
}