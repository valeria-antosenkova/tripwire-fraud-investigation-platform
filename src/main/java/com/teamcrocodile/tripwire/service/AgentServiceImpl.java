package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.dao.AgentDao;
import com.teamcrocodile.tripwire.model.Agent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentDao agentDao;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public AgentServiceImpl(AgentDao agentDao) {
        this.agentDao = agentDao;
    }

     @Override
    public Agent getAgentById(int id) {
         return agentDao.getAgentById(id);
     }

      @Override
      public Agent getAgentByEmail(String email) {
          return agentDao.getAgentByEmail(email);
      }

      @Override
      public boolean authenticateAgent(String email, String password) {
          Agent agent = agentDao.getAgentByEmail(email);
            if (agent == null || password == null) {
                return false;
            }

            String stored = agent.getPass_hash();
            if (stored == null) {
                return false;
            }

            if (stored.startsWith("{")) {
                return passwordEncoder.matches(password, stored);
            }

            return password.equals(stored);
      }

    @Override
    public Agent addNewAgent(Agent agent) {
        return agentDao.createAgent(agent);
    }

    @Override
    public Agent updateAgentData(int id, Agent agent) {
        Agent existingAgent = agentDao.getAgentById(id);
        if (existingAgent == null) {
            throw new IllegalArgumentException("Agent with id " + id + " not found");
        }
        existingAgent.setName(agent.getName());
        existingAgent.setEmail(agent.getEmail());
        // Update other fields as necessary
        agentDao.updateAgent(existingAgent);
        return existingAgent;
    }

    @Override
    public void deleteAgent(int id) {
        agentDao.deleteAgent(id);
    }

    @Override
    public List<Agent> getAllAgents() {
        return agentDao.getAllAgents();
    }



}
