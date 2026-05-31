package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.model.Transaction;
import com.teamcrocodile.tripwire.client.dto.DymoResponse;

import java.util.List;

public interface TransactionService {
        Transaction createTransaction(Transaction transaction);
        List<Transaction> getAllTransactions();
        Transaction getTransactionById(int id);
        Transaction updateTransaction(Transaction transaction);
        DymoResponse scoreTransaction(int id);
        void deleteTransaction(int id);
        Transaction assignTransaction(int transactionId, int agentId);
        Transaction unassignTransaction(int transactionId, int agentId);
        List<Transaction> getTransactionsByAgentId(int agentId);
}
