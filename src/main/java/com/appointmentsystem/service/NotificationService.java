package com.appointmentsystem.service;

import com.appointmentsystem.dto.NotificationRequest;
import com.appointmentsystem.dto.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final RestTemplate restTemplate;

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NotificationResponse sendConfirmation(long studentId, long slotId) {
        NotificationRequest request = new NotificationRequest(
                studentId, slotId,
                "Your appointment for slot " + slotId + " is confirmed.");
        try {
            return restTemplate.postForObject(
                    "http://localhost:8080/mock/notify",
                    request,
                    NotificationResponse.class);
        } catch (RuntimeException ex) {
            log.error("Notification failed: student={} slot={}", studentId, slotId, ex);
            throw ex;
        }
    }
}
