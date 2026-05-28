package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.model.Agent;
import com.teamcrocodile.tripwire.service.AgentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}