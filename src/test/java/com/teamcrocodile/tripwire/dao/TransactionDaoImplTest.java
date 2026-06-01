package com.teamcrocodile.tripwire.dao;

import com.teamcrocodile.tripwire.model.Account;
import com.teamcrocodile.tripwire.model.Agent;
import com.teamcrocodile.tripwire.model.Status;
import com.teamcrocodile.tripwire.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class TransactionDaoImplTest {

    @Autowired
    private TransactionDaoImpl transactionDao;

    @Autowired
    private AccountDaoImpl accountDao;

    @Test
    void createTransaction_canBeRetrievedById() {

        Account account = new Account();
        account.setEmail("test@test.com");
        account.setIpAddress("192.168.1.1");
        account.setIban("GB29NWBK60161331926819");
        account.setPhoneNumber("07911123456");
        accountDao.createAccount(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId()); // generated ID, not hardcoded 1
        transaction.setAmount(new BigDecimal("250.00"));
        transaction.setCurrency("GBP");
        transaction.setRiskScore(72.0);
        transaction.setStatus(Status.UNASSIGNED);

        transactionDao.createTransaction(transaction);
        Transaction retrieved = transactionDao.getTransactionById(transaction.getId());

        assertEquals(new BigDecimal("250.00"), retrieved.getAmount());
        assertEquals(Status.UNASSIGNED, retrieved.getStatus());
    }

    @Autowired
    private AgentDaoImpl agentDao;

    @Test
    void assignTransaction_setsAgentAndMovesToUnderReview() {

        Account account = new Account();
        account.setEmail("test@test.com");
        account.setIpAddress("192.168.1.1");
        account.setIban("GB29NWBK60161331926819");
        account.setPhoneNumber("07911123456");
        accountDao.createAccount(account);

        Agent agent = new Agent();
        agent.setName("Test Agent");
        agent.setEmail("agent@test.com");
        agent.setPass_hash("hashedpassword");
        agentDao.createAgent(agent);

        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId());
        transaction.setAmount(new BigDecimal("250.00"));
        transaction.setCurrency("GBP");
        transaction.setRiskScore(72.0);
        transaction.setStatus(Status.UNASSIGNED);
        transactionDao.createTransaction(transaction);

        transactionDao.assignTransaction(transaction.getId(), agent.getId());

        Transaction retrieved = transactionDao.getTransactionById(transaction.getId());
        assertEquals(agent.getId(), retrieved.getAgentId());
        assertEquals(Status.UNDER_REVIEW, retrieved.getStatus());
    }


    @Test
    void getTransactionsByAgentId(){

        Account account = new Account();
        account.setEmail("fahhhh@test.com");
        account.setIpAddress("67.67.21.9");
        account.setIban("AHHHHHHHHHHHHH39232");
        account.setPhoneNumber("07123456819");
        accountDao.createAccount(account);

        Agent agent = new Agent();
        agent.setName("Test Agent NUMBER TWOOOO");
        agent.setEmail("2nd_agent@test.com");
        agent.setPass_hash("hashedpassword");
        agentDao.createAgent(agent);

        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId());
        transaction.setAmount(new BigDecimal("671.00"));
        transaction.setCurrency("USD");
        transaction.setRiskScore(91.0);
        transaction.setAgentId(agent.getId());
        transaction.setStatus(Status.UNDER_REVIEW);
        transactionDao.createTransaction(transaction);

        List<Transaction> transactionsList = transactionDao.getTransactionsByAgentId(agent.getId());

        assertEquals(1, transactionsList.size());
        assertEquals(transaction.getId(), transactionsList.getFirst().getId());
        assertEquals(agent.getId(), transactionsList.getFirst().getAgentId());

    }








}