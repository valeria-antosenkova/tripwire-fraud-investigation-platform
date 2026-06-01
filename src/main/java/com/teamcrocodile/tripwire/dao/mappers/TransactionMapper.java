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
        Integer agentId = (Integer) rs.getObject("agent_id");
        transaction.setAgentId(agentId);
        transaction.setAccountId(rs.getInt("account_id"));
        transaction.setStatus(Status.fromId(rs.getInt("status_id")));        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setCurrency(rs.getString("currency"));
        transaction.setRiskScore(rs.getDouble("risk_score"));
        transaction.setReasonText(rs.getString("reason_text"));
        transaction.setAgentNote(rs.getString("agent_note"));
        transaction.setOrderId(rs.getString("order_id"));
        transaction.setOrderDate(rs.getString("order_date"));
        transaction.setItems(rs.getString("items"));
        transaction.setPaymentMethod(rs.getString("payment_method"));
        transaction.setShippingAddress(rs.getString("shipping_address"));
        transaction.setBillingAddress(rs.getString("billing_address"));
        transaction.setCreatedAt(rs.getTimestamp("created_at"));
        return transaction;
    }
}