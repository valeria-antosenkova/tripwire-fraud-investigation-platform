package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.dao.mappers.AccountMapper;
import com.teamcrocodile.tripwire.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao{

    private final JdbcTemplate jdbc;

    public AccountDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public Account createAccount(Account account) {

        final String INSERT_ACCOUNT = "INSERT INTO account (email, ip_address, iban, phone_number) VALUES (?, ?, ?, ?) ";
        jdbc.update(INSERT_ACCOUNT, account.getEmail(), account.getIpAddress(), account.getIban(), account.getPhoneNumber());

        return account;
    }

    @Override
    public List<Account> getAllAccounts() {

        final String SELECT_ALL_ACCOUNTS = "SELECT * FROM account";
        return jdbc.query(SELECT_ALL_ACCOUNTS, new AccountMapper());

    }

    @Override
    public Account getAccountById(int id) {

        final String SELECT_ACCOUNT_BY_ID = "SELECT * FROM account WHERE id = ?";
        return jdbc.queryForObject(SELECT_ACCOUNT_BY_ID, new AccountMapper() , id);

    }

    @Override
    public void updateAccount(Account account) {

        final String UPDATE_ACCOUNT = "UPDATE account SET email = ?, ip_address = ?, iban = ?, phone_number = ?";
        jdbc.update(UPDATE_ACCOUNT, account.getEmail(), account.getIpAddress(), account.getIban(), account.getPhoneNumber());

    }

    @Override
    public void deleteAc(int id) {

        final String DELETE_ACCOUNT = "DELETE FROM account WHERE id = ?";
        jdbc.update(DELETE_ACCOUNT, id);

    }
}
