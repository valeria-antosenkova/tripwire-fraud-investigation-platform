package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.dao.mappers.AccountMapper;
import com.teamcrocodile.tripwire.dao.mappers.TransactionMapper;
import com.teamcrocodile.tripwire.model.Account;
import com.teamcrocodile.tripwire.model.Status;
import com.teamcrocodile.tripwire.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TransactionDaoImpl implements TransactionDao{

    private static final Logger log = LoggerFactory.getLogger(TransactionDaoImpl.class);


    private final JdbcTemplate jdbc;

    public TransactionDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction) {

        final String INSERT_TRANSACTION = "INSERT INTO transactions (agent_id, account_id, amount, currency," +
                " risk_score, status_id) VALUES (?, ?, ?, ?, ?, ?) ";

        jdbc.update(INSERT_TRANSACTION,
                transaction.getAgentId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getRiskScore(),
                getStatusId(transaction.getStatus()));
        return transaction;
    }

    //helper function since Status is not of type int
    private int getStatusId(Status status) {
        return switch (status) {
            case APPROVED -> 2;
            case DENIED -> 3;
            case UNASSIGNED -> 4;
            case UNDER_REVIEW -> 1;
        };
    }


    @Override
    public List<Transaction> getAllTransactions() {
        //TODO
        final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM transactions";
        return jdbc.query(SELECT_ALL_TRANSACTIONS, new TransactionMapper());
    }

    @Override
    public Transaction getTransactionById(int id) {
        //TODO
        final String SELECT_TRANSACTION_BY_ID = "SELECT * FROM transactions WHERE transaction_id = ?";
        return jdbc.queryForObject(SELECT_TRANSACTION_BY_ID, new TransactionMapper(), id);
    }

    @Override
    public Account getAccountById(int id) {
        final String SELECT_ACCOUNT_BY_ID = "SELECT * FROM account WHERE account_id = ?";
        return jdbc.queryForObject(SELECT_ACCOUNT_BY_ID, new AccountMapper(), id);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        //TODO
        final String UPDATE_TRANSACTIONS =
                "UPDATE transactions " +
                        "SET agent_id = ?, status_id = ?, risk_score = ?, amount = ? " +
                        "WHERE transaction_id = ?";
        jdbc.update(
                UPDATE_TRANSACTIONS,
                transaction.getAgentId(),
                getStatusId(transaction.getStatus()),
                transaction.getRiskScore(),
                transaction.getAmount(),
                transaction.getId()
        );
    }

    @Override
    public void deleteTransaction(int id) {
        final String DELETE_TRANSACTION = "DELETE FROM transactions WHERE transaction_id = ?";
        jdbc.update(DELETE_TRANSACTION, id);
    }

    @Override
    @Transactional
    public void assignTransaction(int transactionId, int agentId) {
        // Set agent_id and move status to UNDER_REVIEW (status_id = 1)
        final String SQL = "UPDATE transactions SET agent_id = ?, status_id = 1 WHERE transaction_id = ?";
        jdbc.update(SQL, agentId, transactionId);
    }

    @Override
    @Transactional
    public void unassignTransaction(int transactionId, int agentId) {
        // Only unassign if the transaction is currently assigned to the requesting agent
        // Clear agent_id and set status to UNASSIGNED (status_id = 4)
        final String SQL = "UPDATE transactions SET agent_id = NULL, status_id = 4 WHERE transaction_id = ? AND agent_id = ?";
        jdbc.update(SQL, transactionId, agentId);
    }

    @Override
    public List<Transaction> getTransactionsByAgentId(int agentId) {
        final String SQL = "SELECT * FROM transactions WHERE agent_id = ?";
        return jdbc.query(SQL, new TransactionMapper(), agentId);
    }

}
