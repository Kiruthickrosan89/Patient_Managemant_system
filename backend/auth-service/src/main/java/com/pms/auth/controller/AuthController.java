// src/main/java/com/pms/auth/controller/AuthController.java
package com.pms.auth.controller;

import com.pms.auth.dto.AuthResponse;
import com.pms.auth.dto.LoginRequest;
import com.pms.auth.dto.RegisterRequest;
import com.pms.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(authService.validateToken(token));
    }
}