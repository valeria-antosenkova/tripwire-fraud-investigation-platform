package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Agent;
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

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", agent.getName())
                .addValue("email", agent.getEmail())
                .addValue("password_hash", agent.getPass_hash());

        Number generatedId = insert.executeAndReturnKey(params);
        agent.setId(generatedId.intValue());
        return agent;
    }

    @Override
    public List<Agent> getAllAgents() {

        final String sql = "SELECT * FROM Agent;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Agent agent = new Agent();
            agent.setId(rs.getInt("agent_id"));
            agent.setName(rs.getString("name"));
            agent.setEmail(rs.getString("email"));
            agent.setPass_hash(rs.getString("password_hash"));
            agent.setProfile_picture(rs.getString("profile_picture"));
            return agent;
        });
    }

    @Override
    public Agent getAgentById(int id) {

        final String sql = "SELECT * FROM Agent WHERE agent_id = ?;";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Agent agent = new Agent();
            agent.setId(rs.getInt("agent_id"));
            agent.setName(rs.getString("name"));
            agent.setEmail(rs.getString("email"));
            agent.setPass_hash(rs.getString("password_hash"));
            agent.setProfile_picture(rs.getString("profile_picture"));
            return agent;
        });
    }

    @Override
    public Agent getAgentByEmail(String email) {
        final String sql = "SELECT * FROM Agent WHERE email = ?;";
        return jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) -> {
            Agent agent = new Agent();
            agent.setId(rs.getInt("agent_id"));
            agent.setName(rs.getString("name"));
            agent.setEmail(rs.getString("email"));
            agent.setPass_hash(rs.getString("password_hash"));
            agent.setProfile_picture(rs.getString("profile_picture"));
            return agent;
        }).stream().findFirst().orElse(null);
    }

    @Override
    public void updateAgent(Agent agent) {
        final String sql = "UPDATE Agent SET name = ?, email = ?, password_hash = ? WHERE agent_id = ?;";
        jdbcTemplate.update(sql, agent.getName(), agent.getEmail(), agent.getPass_hash(), agent.getId());

    }

    @Override
    public void updatePasswordHash(int id, String passwordHash) {
        final String sql = "UPDATE Agent SET password_hash = ? WHERE agent_id = ?;";
        jdbcTemplate.update(sql, passwordHash, id);
    }

    @Override
    public void deleteAgent(int id) {

        final String sql = "DELETE FROM Agent WHERE agent_id = ?;";
        jdbcTemplate.update(sql, id);

    }

    @Override
    public void updateProfilePicture(int id, String profilePicture) {
        final String sql = "UPDATE Agent SET profile_picture = ? WHERE agent_id = ?;";
        jdbcTemplate.update(sql, profilePicture, id);
    }
}
