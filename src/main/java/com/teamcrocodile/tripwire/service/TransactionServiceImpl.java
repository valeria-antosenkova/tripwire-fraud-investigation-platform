package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.dao.TransactionDao;
import com.teamcrocodile.tripwire.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

private final TransactionDao transactionDao;

    public TransactionServiceImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {

        return transactionDao.createTransaction(transaction);

    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    @Override
    public Transaction findTransactionById(int id) {

        return transactionDao.findTransactionById(id);

    }

    @Override
    public void updateTransaction(Transaction transaction) {

        transactionDao.updateTransaction(transaction);

    }

    @Override
    public void deleteTransaction(int id) {

        transactionDao.deleteTransaction(id);

    }
}
