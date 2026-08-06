package com.pms.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Fallback responses returned when a downstream service circuit opens.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> authFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "Auth Service is currently unavailable. Please try again later.",
                        "service", "auth-service"
                )
        );
    }

    @GetMapping("/service")
    public ResponseEntity<Map<String, Object>> genericFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "The requested service is currently unavailable. Please try again later."
                )
        );
    }
}
