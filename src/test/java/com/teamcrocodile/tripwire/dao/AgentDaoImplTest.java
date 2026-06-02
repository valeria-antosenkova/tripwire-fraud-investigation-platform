package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Agent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AgentDaoImplTest {

    @Autowired
    private AgentDaoImpl agentDao;

    private Agent buildAgent(String name, String email) {
        Agent agent = new Agent();
        agent.setName(name);
        agent.setEmail(email);
        agent.setPass_hash("hashedpassword");
        return agent;
    }

    @Test
    void createAgent() {
        Agent agent = buildAgent("Alice", "alice@test.com");

        agentDao.createAgent(agent);

        assertTrue(agent.getId() > 0);

        Agent retrieved = agentDao.getAgentById(agent.getId());
        assertEquals("Alice", retrieved.getName());
        assertEquals("alice@test.com", retrieved.getEmail());
        assertEquals("hashedpassword", retrieved.getPass_hash());
    }

    @Test
    void getAllAgents() {
        agentDao.createAgent(buildAgent("Alice", "alice@test.com"));
        agentDao.createAgent(buildAgent("Bob", "bob@test.com"));

        List<Agent> agents = agentDao.getAllAgents();

        assertEquals(2, agents.size());
    }

    @Test
    void getAgentById() {
        Agent agent = buildAgent("Charlie", "charlie@test.com");
        agentDao.createAgent(agent);

        Agent retrieved = agentDao.getAgentById(agent.getId());

        assertEquals(agent.getId(), retrieved.getId());
        assertEquals("Charlie", retrieved.getName());
        assertEquals("charlie@test.com", retrieved.getEmail());
    }

    @Test
    void getAgentByEmail() {
        Agent agent = buildAgent("Diana", "diana@test.com");
        agentDao.createAgent(agent);

        Agent retrieved = agentDao.getAgentByEmail("diana@test.com");

        assertNotNull(retrieved);
        assertEquals(agent.getId(), retrieved.getId());
        assertEquals("Diana", retrieved.getName());
    }

    @Test
    void updateAgent() {
        Agent agent = buildAgent("Eve", "eve@test.com");
        agentDao.createAgent(agent);

        agent.setName("Evelyn");
        agent.setEmail("evelyn@test.com");
        agent.setPass_hash("newhashedpassword");
        agentDao.updateAgent(agent);

        Agent updated = agentDao.getAgentById(agent.getId());
        assertEquals("Evelyn", updated.getName());
        assertEquals("evelyn@test.com", updated.getEmail());
        assertEquals("newhashedpassword", updated.getPass_hash());
    }

    @Test
    void updatePasswordHash() {
        Agent agent = buildAgent("Frank", "frank@test.com");
        agentDao.createAgent(agent);

        agentDao.updatePasswordHash(agent.getId(), "brandnewhashedpassword");

        Agent updated = agentDao.getAgentById(agent.getId());
        assertEquals("brandnewhashedpassword", updated.getPass_hash());
        assertEquals("Frank", updated.getName());
        assertEquals("frank@test.com", updated.getEmail());
    }

    @Test
    void deleteAgent() {
        Agent agent = buildAgent("Grace", "grace@test.com");
        agentDao.createAgent(agent);

        int id = agent.getId();
        agentDao.deleteAgent(id);

        assertThrows(Exception.class, () -> agentDao.getAgentById(id));
    }

    @Test
    void updateProfilePicture() {
        Agent agent = buildAgent("Hank", "hank@test.com");
        agentDao.createAgent(agent);

        agentDao.updateProfilePicture(agent.getId(), "https://example.com/pics/hank.jpg");

        Agent updated = agentDao.getAgentById(agent.getId());
        assertEquals("https://example.com/pics/hank.jpg", updated.getProfile_picture());
    }
}