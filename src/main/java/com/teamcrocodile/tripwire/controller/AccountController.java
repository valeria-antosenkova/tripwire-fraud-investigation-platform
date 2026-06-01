package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.dao.AccountDao;
import com.teamcrocodile.tripwire.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable int id) {
        return accountDao.getAccountById(id);
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountDao.createAccount(account);
    }
}

