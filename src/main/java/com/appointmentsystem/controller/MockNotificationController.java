package com.appointmentsystem.controller;

import com.appointmentsystem.dto.NotificationRequest;
import com.appointmentsystem.dto.NotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockNotificationController {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/notify")
    public NotificationResponse notify(@RequestBody NotificationRequest request) {
        System.out.printf("Mock notification: student=%d slot=%d — %s%n",
                request.getStudentId(), request.getSlotId(), request.getMessage());
        return new NotificationResponse("SENT",
                "Confirmation sent for slot " + request.getSlotId());
    }
}
