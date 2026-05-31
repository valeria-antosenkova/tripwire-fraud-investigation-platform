package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final AgentService agentService;

    public AuthController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        boolean authenticated = agentService.authenticateAgent(request.email(), request.password());

        if (!authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Invalid email or password"));
        }

        return ResponseEntity.ok(new LoginResponse(true, "Login successful"));
    }

    public record LoginRequest(String email, String password) {}
    public record LoginResponse(boolean success, String message) {}
}

