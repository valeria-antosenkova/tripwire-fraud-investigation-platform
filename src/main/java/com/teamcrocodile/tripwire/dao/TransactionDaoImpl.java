package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionDaoImpl implements TransactionDao{
    @Override
    public Transaction createTransaction(Transaction transaction) {
        //TODO
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        //TODO
        return null;
    }

    @Override
    public Transaction findTransactionById(int id) {
        //TODO
        return null;
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        //TODO

    }

    @Override
    public void deleteTransaction(int id) {
        //TODO

    }
}
