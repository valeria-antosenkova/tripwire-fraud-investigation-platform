package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.model.Agent;

import java.util.List;

public class AgentServiceImpl implements AgentService {

    private final AgentService agentService;

    public AgentServiceImpl(AgentService agentService) {
        this.agentService = agentService;
    }

     @Override
    public Agent getAgentById(int id) {
         return agentService.getAgentById(id);
     }

    @Override
    public Agent addNewAgent(Agent agent) {
        return agentService.addNewAgent(agent);
    }

    @Override
    public Agent updateAgentData(int id, Agent agent) {
        return agentService.updateAgentData(id, agent);
    }

    @Override
    public void deleteAgent(int id) {
        agentService.deleteAgent(id);
    }

    @Override
    public List<Agent> getAllAgents() {
        return agentService.getAllAgents();
    }



}
