package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.dao.mappers.TransactionMapper;
import com.teamcrocodile.tripwire.model.Status;
import com.teamcrocodile.tripwire.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
            case APPROVED -> 1;
            case DENIED -> 2;
            case UNASSIGNED -> 3;
            case UNDER_REVIEW -> 4;
        };
    }


    @Override
    public List<Transaction> getAllTransactions() {
        //TODO
        final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM transactions";
        return jdbc.query(SELECT_ALL_TRANSACTIONS, new TransactionMapper());
    }

    @Override
    public Transaction findTransactionById(int id) {
        //TODO
        final String SELECT_TRANSACTION_BY_ID = "SELECT * FROM transactions WHERE transaction_id = ?";
        return jdbc.queryForObject(SELECT_TRANSACTION_BY_ID, new TransactionMapper(), id);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        //TODO
        final String UPDATE_TRANSACTIONS = "UPDATE transactions SET agent_id = ?, status_id = ?, risk_score = ? WHERE transaction_id = ?";
        jdbc.update(UPDATE_TRANSACTIONS, transaction.getAgentId(), getStatusId(
                transaction.getStatus()), transaction.getRiskScore(),transaction.getId());
    }

    @Override
    public void deleteTransaction(int id) {
        //TODO
        final String DELETE_TRANSACTION = "DELETE FROM transactions WHERE transaction_id = ?";
        jdbc.update(DELETE_TRANSACTION, id);

    }
}
