package com.teamcrocodile.tripwire.dao.mappers;

import com.teamcrocodile.tripwire.model.Account;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();

        account.setId(rs.getInt("account_id"));
        account.setName(rs.getString("name"));
        account.setEmail(rs.getString("email"));
        account.setIpAddress(rs.getString("ip_address"));
        account.setIban(rs.getString("iban"));
        account.setCreatedAt(rs.getString("created_at"));
        account.setPhoneNumber(rs.getString("phone_number"));

        return account;
    }
}