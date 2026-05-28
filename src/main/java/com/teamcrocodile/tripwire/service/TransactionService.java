package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.model.Transaction;

import java.util.List;

public interface TransactionService {
        Transaction createTransaction(Transaction transaction);
        List<Transaction> getAllTransactions();
        Transaction getTransactionById(int id);
        Transaction updateTransaction(Transaction transaction);
        void deleteTransaction(int id);
}
