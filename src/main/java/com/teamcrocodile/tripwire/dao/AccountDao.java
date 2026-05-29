package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Account;

import java.util.List;

public interface AccountDao {
    Account createAccount(Account account);
    List<Account> getAllAccounts();
    Account getAccountById(int id);
    void updateAccount(Account account);
    void deleteAc(int id);

}
