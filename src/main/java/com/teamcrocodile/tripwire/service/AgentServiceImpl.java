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

            return matchesPassword(password, stored);
      }

    @Override
    public void changePassword(int id, String currentPassword, String newPassword) {
        if (currentPassword == null || currentPassword.isBlank()) {
            throw new IllegalArgumentException("Current password is required");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters");
        }

        Agent agent = agentDao.getAgentById(id);
        if (agent == null) {
            throw new IllegalArgumentException("Agent with id " + id + " not found");
        }

        String stored = agent.getPass_hash();
        if (stored == null || stored.isBlank()) {
            throw new IllegalStateException("Password is not set for this account");
        }
        if (!matchesPassword(currentPassword, stored)) {
            throw new SecurityException("Current password is incorrect");
        }
        if (matchesPassword(newPassword, stored)) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        String encoded = passwordEncoder.encode(newPassword);
        agentDao.updatePasswordHash(id, encoded);
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

    private boolean matchesPassword(String rawPassword, String storedPassword) {
        if (storedPassword.startsWith("{")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }
        return rawPassword.equals(storedPassword);
    }

    @Override
    public void uploadProfilePicture(int id, String imageData) {
        if (imageData == null || imageData.isBlank()) {
            throw new IllegalArgumentException("Image data is required");
        }
        Agent agent = agentDao.getAgentById(id);
        if (agent == null) {
            throw new IllegalArgumentException("Agent with id " + id + " not found");
        }
        agentDao.updateProfilePicture(id, imageData);
    }

}
