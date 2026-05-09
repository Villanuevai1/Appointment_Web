package com.appointmentsystem.service;

import com.appointmentsystem.dto.NotificationRequest;
import com.appointmentsystem.dto.NotificationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void sendConfirmationPostsToMockNotificationEndpoint() {
        NotificationResponse response = new NotificationResponse("SENT", "Confirmation sent for slot 12");
        when(restTemplate.postForObject(
                eq("http://localhost:8080/mock/notify"),
                any(NotificationRequest.class),
                eq(NotificationResponse.class))).thenReturn(response);

        NotificationResponse actual = notificationService.sendConfirmation(99L, 12L);

        ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(restTemplate).postForObject(
                eq("http://localhost:8080/mock/notify"),
                requestCaptor.capture(),
                eq(NotificationResponse.class));

        assertEquals(99L, requestCaptor.getValue().getStudentId());
        assertEquals(12L, requestCaptor.getValue().getSlotId());
        assertEquals("Your appointment for slot 12 is confirmed.", requestCaptor.getValue().getMessage());
        assertEquals("SENT", actual.getStatus());
        assertEquals("Confirmation sent for slot 12", actual.getMessage());
    }
}