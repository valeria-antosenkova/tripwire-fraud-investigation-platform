package com.teamcrocodile.tripwire.dao.mappers;

import com.teamcrocodile.tripwire.model.Status;
import com.teamcrocodile.tripwire.model.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {

        Transaction transaction = new Transaction();

        transaction.setId(rs.getInt("transaction_id"));
        transaction.setAgentId(rs.getInt("agent_id"));
        transaction.setAccountId(rs.getInt("account_id"));
        transaction.setStatus(Status.valueOf(rs.getString("status")));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setCurrency(rs.getString("currency"));
        transaction.setRiskScore(rs.getDouble("risk_score"));
        transaction.setReasonId(rs.getString("reason_text"));
        transaction.setCreatedAt(rs.getTimestamp("created_at"));
        return transaction;
    }
}