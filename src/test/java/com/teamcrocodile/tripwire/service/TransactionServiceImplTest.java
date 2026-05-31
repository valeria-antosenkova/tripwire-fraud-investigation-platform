package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.client.dto.DymoResponse;
import com.teamcrocodile.tripwire.dao.TransactionDao;
import com.teamcrocodile.tripwire.model.Account;
import com.teamcrocodile.tripwire.model.Status;
import com.teamcrocodile.tripwire.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private DymoService dymoService;

    @Test
    void createTransaction_defaultsNewCasesToUnassigned() {
        TransactionServiceImpl service = new TransactionServiceImpl(transactionDao, dymoService);

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setAccountId(10);
        transaction.setAgentId(7);
        transaction.setStatus(Status.APPROVED);
        transaction.setAmount(new BigDecimal("3499.00"));

        Account account = new Account();
        account.setId(10);
        DymoResponse response = new DymoResponse();
        response.setScore(84.0);
        response.setApiAvailable(true);

        when(transactionDao.getAccountById(10)).thenReturn(account);
        when(dymoService.checkAccount(account)).thenReturn(response);
        when(transactionDao.createTransaction(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction created = service.createTransaction(transaction);

        assertEquals(Status.UNASSIGNED, created.getStatus());
        assertNull(created.getAgentId());
        assertEquals(84.0, created.getRiskScore());
        verify(transactionDao).createTransaction(transaction);
    }

    @Test
    void updateTransaction_rejectsApprovalForUnassignedCase() {
        TransactionServiceImpl service = new TransactionServiceImpl(transactionDao, dymoService);

        Transaction current = new Transaction();
        current.setId(1);
        current.setAccountId(10);
        current.setStatus(Status.UNASSIGNED);
        current.setAgentId(null);

        Transaction update = new Transaction();
        update.setId(1);
        update.setAccountId(10);
        update.setStatus(Status.APPROVED);
        update.setAgentId(99);

        when(transactionDao.getTransactionById(1)).thenReturn(current);

        assertThrows(IllegalStateException.class, () -> service.updateTransaction(update));
        verify(transactionDao, never()).updateTransaction(any());
    }

    @Test
    void updateTransaction_allowsApprovalWhenAssignedAndUnderReview() {
        TransactionServiceImpl service = new TransactionServiceImpl(transactionDao, dymoService);

        Transaction current = new Transaction();
        current.setId(1);
        current.setAccountId(10);
        current.setStatus(Status.UNDER_REVIEW);
        current.setAgentId(7);

        Transaction update = new Transaction();
        update.setId(1);
        update.setAccountId(10);
        update.setStatus(Status.APPROVED);
        update.setAgentId(99);
        update.setAmount(new BigDecimal("125.00"));

        Account account = new Account();
        account.setId(10);
        DymoResponse response = new DymoResponse();
        response.setScore(92.0);
        response.setApiAvailable(true);

        when(transactionDao.getTransactionById(1)).thenReturn(current);
        when(transactionDao.getAccountById(10)).thenReturn(account);
        when(dymoService.checkAccount(account)).thenReturn(response);
        doNothing().when(transactionDao).updateTransaction(any(Transaction.class));

        Transaction result = service.updateTransaction(update);

        assertEquals(Status.APPROVED, result.getStatus());
        assertEquals(7, result.getAgentId());
        assertEquals(92.0, result.getRiskScore());
        verify(transactionDao).updateTransaction(update);
    }
}



