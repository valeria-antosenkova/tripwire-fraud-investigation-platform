package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.dao.TransactionDao;
import com.teamcrocodile.tripwire.client.dto.DymoResponse;
import com.teamcrocodile.tripwire.model.Account;
import com.teamcrocodile.tripwire.model.Status;
import com.teamcrocodile.tripwire.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final DymoService dymoService;


    public TransactionServiceImpl(TransactionDao transactionDao, DymoService dymoService) {
        this.dymoService = dymoService;
        this.transactionDao = transactionDao;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        scoreTransaction(transaction);
        return transactionDao.createTransaction(transaction);

    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    @Override
    public Transaction getTransactionById(int id) {

        return transactionDao.getTransactionById(id);

    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        scoreTransaction(transaction);
        transactionDao.updateTransaction(transaction);

        return transaction;
    }

    @Override
    public void deleteTransaction(int id) {
        transactionDao.deleteTransaction(id);
    }

    @Override
    public Transaction assignTransaction(int transactionId, int agentId) {
        transactionDao.assignTransaction(transactionId, agentId);
        return transactionDao.getTransactionById(transactionId);
    }

    @Override
    public Transaction unassignTransaction(int transactionId, int agentId) {
        transactionDao.unassignTransaction(transactionId, agentId);
        return transactionDao.getTransactionById(transactionId);
    }

    @Override
    public List<Transaction> getTransactionsByAgentId(int agentId) {
        return transactionDao.getTransactionsByAgentId(agentId);
    }

    @Override
    public DymoResponse scoreTransaction(int id) {
        Transaction transaction = transactionDao.getTransactionById(id);
        DymoResponse response = scoreTransaction(transaction);
        transactionDao.updateTransaction(transaction);
        return response;
    }

    private DymoResponse scoreTransaction(Transaction transaction) {
        Account account = transactionDao.getAccountById(transaction.getAccountId());
        DymoResponse response = dymoService.checkAccount(account);
        transaction.setRiskScore(response.getScore());
        return response;
    }


}
