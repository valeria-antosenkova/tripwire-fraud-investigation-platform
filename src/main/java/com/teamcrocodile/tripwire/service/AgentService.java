package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.model.Agent;

import java.util.List;

public interface AgentService {


    Agent addNewAgent(Agent agent);
    List<Agent> getAllAgents();
    Agent getAgentById(int id);
    Agent getAgentByEmail(String email);
    boolean authenticateAgent(String email, String password);
    Agent updateAgentData(int id, Agent agent);
    void deleteAgent(int id);
}
