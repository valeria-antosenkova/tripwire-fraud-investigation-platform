package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Agent;

import java.util.List;

public interface AgentDao {

    Agent createAgent(Agent agent);
    List<Agent> getAllAgents();
    Agent getAgentById(int id);
    Agent getAgentByEmail(String email);
    void updateAgent(Agent agent);
    void deleteAgent(int id);

}
