package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.service.AgentServiceImpl;
import com.teamcrocodile.tripwire.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
@CrossOrigin
public class AgentController {

    @Autowired
    AgentServiceImpl AgentService;

    @GetMapping("/agents")
    public String getAllAgents() {
        //TODO
        return null;
    }

    @GetMapping("/{id}")
    public String getAgentById() {
        //TODO
        return null;
    }

    @PostMapping("/add")
    public String addAgent() {
        //TODO
        return null;
    }

    @PutMapping("/{id}")
    public String updateAgent() {
        //TODO
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteAgent() {
        //TODO
    }



}