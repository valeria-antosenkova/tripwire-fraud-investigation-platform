package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.client.dto.DymoResponse;
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

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable int id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping("/add")
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
        // Make sure the path id is authoritative
        transaction.setId(id);

        // Perform the update
        transactionService.updateTransaction(transaction);

        // Return the updated resource (fetch from DB to ensure latest state)
        return transactionService.getTransactionById(id);
    }

    @PostMapping("/{id}/score")
    public DymoResponse scoreTransaction(@PathVariable int id) {
        return transactionService.scoreTransaction(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable int id) {
        transactionService.deleteTransaction(id);
    }

    // Assign a transaction to an agent — sets agent_id and moves status to UNDER_REVIEW
    @PutMapping("/{id}/assign/{agentId}")
    public Transaction assignTransaction(@PathVariable int id, @PathVariable int agentId) {
        return transactionService.assignTransaction(id, agentId);
    }

    // Get all transactions assigned to a specific agent
    @GetMapping("/agent/{agentId}")
    public List<Transaction> getTransactionsByAgent(@PathVariable int agentId) {
        return transactionService.getTransactionsByAgentId(agentId);
    }

}
