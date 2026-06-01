package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountDaoImplTest {

    @Autowired
    private AccountDaoImpl accountDao;

    @Test
    void createAccount_canBeRetrievedById() {

        Account account = new Account();
        account.setName("Bob");
        account.setEmail("bob@test.com");
        account.setIpAddress("192.168.1.1");
        account.setIban("GB29NWBK60161331926819");
        account.setPhoneNumber("07911123456");

        accountDao.createAccount(account);

        Account retrieved = accountDao.getAccountById(account.getId());

        assertEquals("Bob", retrieved.getName());
        assertEquals("bob@test.com", retrieved.getEmail());
        assertEquals("192.168.1.1", retrieved.getIpAddress());
        assertEquals("GB29NWBK60161331926819", retrieved.getIban());
        assertEquals("07911123456", retrieved.getPhoneNumber());
    }

    @Test
    void getAllAccounts_returnsInsertedAccounts() {

        Account account1 = new Account();
        account1.setName("Bob");
        account1.setEmail("bob@test.com");
        account1.setIpAddress("1.1.1.1");
        account1.setIban("GB111");
        account1.setPhoneNumber("111");

        Account account2 = new Account();
        account2.setName("Alice");
        account2.setEmail("alice@test.com");
        account2.setIpAddress("2.2.2.2");
        account2.setIban("GB222");
        account2.setPhoneNumber("222");

        accountDao.createAccount(account1);
        accountDao.createAccount(account2);

        List<Account> accounts = accountDao.getAllAccounts();

        assertEquals(2, accounts.size());
    }

    @Test
    void updateAccount_updatesStoredValues() {

        Account account = new Account();
        account.setName("Bob");
        account.setEmail("bob@test.com");
        account.setIpAddress("1.1.1.1");
        account.setIban("GB111");
        account.setPhoneNumber("111");

        accountDao.createAccount(account);

        account.setName("Robert");
        account.setEmail("robert@test.com");
        account.setIpAddress("10.10.10.10");
        account.setIban("GB999");
        account.setPhoneNumber("999");

        accountDao.updateAccount(account);

        Account updated = accountDao.getAccountById(account.getId());

        assertEquals("Robert", updated.getName());
        assertEquals("robert@test.com", updated.getEmail());
        assertEquals("10.10.10.10", updated.getIpAddress());
        assertEquals("GB999", updated.getIban());
        assertEquals("999", updated.getPhoneNumber());
    }

    @Test
    void deleteAccount_removesAccount() {

        Account account = new Account();
        account.setName("Bob");
        account.setEmail("bob@test.com");
        account.setIpAddress("1.1.1.1");
        account.setIban("GB111");
        account.setPhoneNumber("111");

        accountDao.createAccount(account);

        int id = account.getId();

        accountDao.deleteAc(id);

        assertThrows(
                Exception.class,
                () -> accountDao.getAccountById(id)
        );
    }
}