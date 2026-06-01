package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Agent;
import com.teamcrocodile.tripwire.model.AgentRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AgentDaoImpl implements AgentDao {

    private final JdbcTemplate jdbcTemplate;

    public AgentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    @Override
    @Transactional
    public Agent createAgent(Agent agent) {

            final String sql = "INSERT INTO Agent(name, email, password_hash) VALUES(?,?,?);";
            jdbcTemplate.update(sql, agent.getName(), agent.getEmail(), agent.getPass_hash());
            return agent;
    } */

    @Override
    @Transactional
    public Agent createAgent(Agent agent) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Agent")
                .usingGeneratedKeyColumns("agent_id");

        String role = agent.getRole() != null ? agent.getRole() : AgentRole.ANALYST.name();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", agent.getName())
                .addValue("email", agent.getEmail())
                .addValue("password_hash", agent.getPass_hash())
                .addValue("role", role);

        Number generatedId = insert.executeAndReturnKey(params);
        agent.setId(generatedId.intValue());
        agent.setRole(role);
        return agent;
    }

    @Override
    public List<Agent> getAllAgents() {

        final String sql = "SELECT * FROM Agent ORDER BY agent_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapAgent(rs));
    }

    @Override
    public Agent getAgentById(int id) {

        final String sql = "SELECT * FROM Agent WHERE agent_id = ?";
        List<Agent> agents = jdbcTemplate.query(sql, (rs, rowNum) -> mapAgent(rs), id);
        return agents.isEmpty() ? null : agents.getFirst();
    }

    @Override
    public Agent getAgentByEmail(String email) {
        final String sql = "SELECT * FROM Agent WHERE LOWER(email) = LOWER(?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapAgent(rs), email)
                .stream().findFirst().orElse(null);
    }

    @Override
    public void updateAgent(Agent agent) {
        final String sql = "UPDATE Agent SET name = ?, email = ?, password_hash = ?, role = ? WHERE agent_id = ?";
        jdbcTemplate.update(sql, agent.getName(), agent.getEmail(), agent.getPass_hash(), agent.getRole(), agent.getId());

    }

    @Override
    public void updatePasswordHash(int id, String passwordHash) {
        final String sql = "UPDATE Agent SET password_hash = ? WHERE agent_id = ?;";
        jdbcTemplate.update(sql, passwordHash, id);
    }

    @Override
    @Transactional
    public void deleteAgent(int id) {
        jdbcTemplate.update("UPDATE Transactions SET agent_id = NULL WHERE agent_id = ?", id);
        jdbcTemplate.update("DELETE FROM Agent WHERE agent_id = ?", id);
    }

    @Override
    public void updateProfilePicture(int id, String profilePicture) {
        final String sql = "UPDATE Agent SET profile_picture = ? WHERE agent_id = ?;";
        jdbcTemplate.update(sql, profilePicture, id);
    }

    private Agent mapAgent(java.sql.ResultSet rs) throws java.sql.SQLException {
        Agent agent = new Agent();
        agent.setId(rs.getInt("agent_id"));
        agent.setName(rs.getString("name"));
        agent.setEmail(rs.getString("email"));
        agent.setPass_hash(rs.getString("password_hash"));
        agent.setRole(rs.getString("role"));
        agent.setProfile_picture(rs.getString("profile_picture"));
        return agent;
    }
}
