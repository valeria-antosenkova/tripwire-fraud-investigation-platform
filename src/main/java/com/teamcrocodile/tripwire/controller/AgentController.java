package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.model.Agent;
import com.teamcrocodile.tripwire.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agent")
@CrossOrigin
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/agents")
    public List<Agent> getAllAgents() {
        return agentService.getAllAgents();
    }

    @GetMapping("/{id}")
    public Agent getAgentById(@PathVariable int id) {
        return agentService.getAgentById(id);
    }

    @PostMapping("/add")
    public Agent addAgent(@RequestBody Agent agent) {
        return agentService.addNewAgent(agent);
    }

    @PutMapping("/{id}")
    public Agent updateAgent(@PathVariable int id, @RequestBody Agent agent) {
        return agentService.updateAgentData(id, agent);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable int id, @RequestBody ChangePasswordRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Request body is required"));
        }
        try {
            agentService.changePassword(id, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (SecurityException ex) {
            return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PutMapping("/{id}/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable int id, @RequestBody Map<String, String> request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Request body is required"));
        }
        try {
            String imageData = request.get("imageData");
            agentService.uploadProfilePicture(id, imageData);
            return ResponseEntity.ok(Map.of("message", "Profile picture updated successfully"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAgent(@PathVariable int id) {
        agentService.deleteAgent(id);
    }

    /**
     * Login endpoint. Accepts { "email": "...", "password": "..." }
     * Returns the agent (without password hash) on success, or 401 on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email    = credentials.get("email");
        String password = credentials.get("password");

        boolean ok = agentService.authenticateAgent(email, password);
        if (!ok) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        Agent agent = agentService.getAgentByEmail(email);
        // Return agent info without the password hash
        return ResponseEntity.ok(Map.of(
                "id",    agent.getId(),
                "name",  agent.getName(),
                "email", agent.getEmail(),
                "profilePicture", agent.getProfile_picture() != null ? agent.getProfile_picture() : ""
        ));
    }

    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

}