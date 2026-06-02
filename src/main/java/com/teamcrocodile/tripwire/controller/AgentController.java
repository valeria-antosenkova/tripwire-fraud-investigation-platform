package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.controller.dto.CreateAgentRequest;
import com.teamcrocodile.tripwire.exception.AdminAccessDeniedException;
import com.teamcrocodile.tripwire.model.Agent;
import com.teamcrocodile.tripwire.model.AgentRole;
import com.teamcrocodile.tripwire.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
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

    @GetMapping("/team")
    public ResponseEntity<?> getTeam(@RequestParam int adminId) {
        try {
            agentService.requireAdmin(adminId);
            List<Map<String, Object>> agents = agentService.getAllAgents().stream()
                    .map(this::toPublicAgent)
                    .toList();
            return ResponseEntity.ok(agents);
        } catch (AdminAccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public Agent getAgentById(@PathVariable int id) {
        return agentService.getAgentById(id);
    }

    @PostMapping("/add")
    public Agent addAgent(@RequestBody Agent agent) {
        return agentService.addNewAgent(agent);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createAgentAsAdmin(@RequestBody CreateAgentRequest request) {
        try {
            Agent created = agentService.createAgentAsAdmin(
                    request.getAdminId(),
                    request.getName(),
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(toPublicAgent(created));
        } catch (AdminAccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
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

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteAgentAsAdmin(@PathVariable int id, @RequestParam int adminId) {
        try {
            agentService.deleteAgentAsAdmin(adminId, id);
            return ResponseEntity.ok(Map.of("message", "Team member removed"));
        } catch (AdminAccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        boolean ok = agentService.authenticateAgent(email, password);
        if (!ok) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        Agent agent = agentService.getAgentByEmail(email);
        return ResponseEntity.ok(toPublicAgent(agent));
    }

    private Map<String, Object> toPublicAgent(Agent agent) {
        AgentRole role = agent.getAgentRole();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", agent.getId());
        body.put("name", agent.getName());
        body.put("email", agent.getEmail());
        body.put("role", role.name());
        body.put("roleLabel", role.displayLabel());
        body.put("profilePicture", agent.getProfile_picture() != null ? agent.getProfile_picture() : "");
        return body;
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
