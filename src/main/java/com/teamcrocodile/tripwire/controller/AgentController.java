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
                "email", agent.getEmail()
        ));
    }

}