package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.model.Transaction;
import com.teamcrocodile.tripwire.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionService;

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {

        return transactionService.getAllTransactions();
    }

    @GetMapping
    public Transaction getTransactionById(@PathVariable int id) {
        return transactionService.getTransactionById(id);
    }


    @PostMapping("/add")
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping
    public Transaction updateTransaction(@RequestBody Transaction transaction) {
        return transactionService.updateTransaction(transaction);
    }


        @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable int id) {
            transactionService.deleteTransaction(id);
        }

}
