package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.service.TransactionServicceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {

    @Autowired
    TransactionServicceImpl transactionService;

    @GetMapping("/transactions")
    public String getAllTransactions() {
        //TODO
        return null;
    }

    @GetMapping("/{id}")
    public String getTransactionById() {
        //TODO
        return null;
    }

    @PostMapping("/add")
    public String addTransaction() {
        //TODO
        return null;
    }

    @PutMapping("/{id}")
    public String updateTransaction() {
        //TODO
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction() {
        //TODO
    }

}
