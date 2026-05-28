package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AgentDaoImpl implements AgentDao {

@Autowired
    private final JdbcTemplate jdbcTemplate;

    public AgentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Agent createAgent(Agent agent) {

            final String sql = "INSERT INTO agent(agentName, agentEmail) VALUES(?,?);";
            jdbcTemplate.update(sql, agent.getName(), agent.getEmail());
            return agent;
    }

    @Override
    public List<Agent> getAllAgents() {

        final String sql = "SELECT * FROM agent;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Agent agent = new Agent();
            agent.setId(rs.getInt("agentId"));
            agent.setName(rs.getString("agentName"));
            agent.setEmail(rs.getString("agentEmail"));
            return agent;
        });
    }

    @Override
    public Agent getAgentById(int id) {

        final String sql = "SELECT * FROM agent WHERE agentId = ?;";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Agent agent = new Agent();
            agent.setId(rs.getInt("agentId"));
            agent.setName(rs.getString("agentName"));
            agent.setEmail(rs.getString("agentEmail"));
            return agent;
        });
    }

    @Override
    public void updateAgent(Agent agent) {
        final String sql = "UPDATE agent SET agentName = ?, agentEmail = ? WHERE agentId = ?;";
        jdbcTemplate.update(sql, agent.getName(), agent.getEmail(), agent.getId());

    }

    @Override
    public void deleteAgent(int id) {

        final String sql = "DELETE FROM agent WHERE agentId = ?;";
        jdbcTemplate.update(sql, id);

    }
}
