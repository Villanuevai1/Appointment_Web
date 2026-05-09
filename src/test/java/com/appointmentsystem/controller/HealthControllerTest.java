package com.appointmentsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HealthController healthController;

    @Test
    void healthReturnsUpWhenDatabaseAndNotificationBoundaryAreReachable() {
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);
        when(restTemplate.getForObject("http://localhost:8080/mock/health", String.class)).thenReturn("OK");

        ResponseEntity<Map<String, String>> response = healthController.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().get("database"));
        assertEquals("UP", response.getBody().get("notification_service"));
        assertEquals("UP", response.getBody().get("status"));
    }

    @Test
    void healthReturnsDownWhenDatabaseCheckFails() {
        doThrow(new RuntimeException("db down")).when(jdbcTemplate).queryForObject("SELECT 1", Integer.class);
        when(restTemplate.getForObject("http://localhost:8080/mock/health", String.class)).thenReturn("OK");

        ResponseEntity<Map<String, String>> response = healthController.health();

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("DOWN", response.getBody().get("database"));
        assertEquals("UP", response.getBody().get("notification_service"));
        assertEquals("DOWN", response.getBody().get("status"));
    }
}