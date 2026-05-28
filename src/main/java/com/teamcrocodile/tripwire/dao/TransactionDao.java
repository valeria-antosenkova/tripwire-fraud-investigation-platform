package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDao {
        Transaction createTransaction(Transaction transaction);
        List<Transaction> getAllTransactions();
        Transaction getTransactionById(int id);
        void updateTransaction(Transaction transaction);
        void deleteTransaction(int id);
}
