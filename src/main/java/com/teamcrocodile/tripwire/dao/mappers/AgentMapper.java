package com.teamcrocodile.tripwire.dao.mappers;

import com.teamcrocodile.tripwire.model.Agent;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AgentMapper implements RowMapper<Agent> {

    @Override
    public Agent mapRow(ResultSet rs, int rowNum) throws SQLException {
        Agent agent = new Agent();
        agent.setId(rs.getInt("agent_id"));
        agent.setName(rs.getString("name"));
        agent.setEmail(rs.getString("email"));
        return agent;
    }
}