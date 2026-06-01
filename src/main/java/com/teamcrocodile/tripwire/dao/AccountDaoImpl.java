package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.dao.mappers.AccountMapper;
import com.teamcrocodile.tripwire.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao {

    private final JdbcTemplate jdbc;

    public AccountDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public Account createAccount(Account account) {
        final String SQL = "INSERT INTO account (name, email, ip_address, iban, phone_number) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQL, new String[]{"ACCOUNT_ID"});
            ps.setString(1, account.getName());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getIpAddress());
            ps.setString(4, account.getIban());
            ps.setString(5, account.getPhoneNumber());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            account.setId(keyHolder.getKey().intValue());
        }
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return jdbc.query("SELECT * FROM account", new AccountMapper());
    }

    @Override
    public Account getAccountById(int id) {
        return jdbc.queryForObject(
                "SELECT * FROM account WHERE account_id = ?",
                new AccountMapper(), id);
    }

    @Override
    public void updateAccount(Account account) {
        final String SQL = "UPDATE account SET name = ?, email = ?, ip_address = ?, iban = ?, phone_number = ? WHERE account_id = ?";
        jdbc.update(SQL, account.getName(), account.getEmail(), account.getIpAddress(), account.getIban(), account.getPhoneNumber(), account.getId());
    }

    @Override
    public void deleteAc(int id) {
        jdbc.update("DELETE FROM account WHERE account_id = ?", id);
    }
}
