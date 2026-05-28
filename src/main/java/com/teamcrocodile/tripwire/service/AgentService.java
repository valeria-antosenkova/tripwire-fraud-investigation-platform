package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.model.Agent;

import java.util.List;

public interface AgentService {


    Agent addNewAgent(Agent agent);
    List<Agent> getAllAgents();
    Agent getAgentById(int id);
    Agent updateAgentData(int id, Agent agent);
    void deleteAgent(int id);
}
