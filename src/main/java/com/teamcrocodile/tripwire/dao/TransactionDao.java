package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Account;
import com.teamcrocodile.tripwire.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDao {
        Transaction createTransaction(Transaction transaction);
        List<Transaction> getAllTransactions();
        Transaction getTransactionById(int id);
        Account getAccountById(int id);
        void updateTransaction(Transaction transaction);
        void deleteTransaction(int id);
        void assignTransaction(int transactionId, int agentId);
        List<Transaction> getTransactionsByAgentId(int agentId);

}
