package com.console.paymentApp.service.impl;

import com.console.paymentApp.entity.Account;
import com.console.paymentApp.enums.TransactionTypeEnum;
import com.console.paymentApp.model.Transaction;
import com.console.paymentApp.repository.AccountRepository;
import com.console.paymentApp.repository.TransactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PaymentServiceImplTest {
    AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    TransactionsRepository transactionsRepository = Mockito.mock(TransactionsRepository.class);
    JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
    PaymentServiceImpl paymentServiceImpl;
    @BeforeEach
    void setup(){
        this.paymentServiceImpl = new PaymentServiceImpl(accountRepository, transactionsRepository);
    }
    @Test
    @DisplayName("Test getAllAccount method returns empty list")
    void getAllAccount_emptyList() throws Exception {
       List<Account> accountList = new ArrayList<>();
       when(accountRepository.fetchAllAccounts()).thenReturn(accountList);
        List<Account> response = paymentServiceImpl.getAllAccount();
        assertEquals(response.size(), accountList.size());
    }
    @Test
    @DisplayName("Test getAllAccount method returns empty list")
    void getAllAccount_withData() throws Exception {
        List<Account> accountList = new ArrayList<>();
        Account account = new Account();
        account.setName("Test User");
        account.setEmail("test@email.com");
        accountList.add(account);
        when(accountRepository.fetchAllAccounts()).thenReturn(accountList);
        List<Account> response = paymentServiceImpl.getAllAccount();
        assertEquals(response.get(0).getName(), accountList.get(0).getName());
        assertEquals(response.get(0).getEmail(), accountList.get(0).getEmail());
    }

    @Test
    @DisplayName("Get all transaction list.")
    void getAllTransaction() throws Exception {
        Object[] trans = new Object[]{Long.valueOf(000),Long.valueOf(10001), Long.valueOf(10002), Long.valueOf(10), LocalDateTime.now(),"Test User1", "Test User2"};
        List<Object[]> results = new ArrayList<>();
        results.add(trans);
        when(transactionsRepository.fetchAllTransaction(Long.valueOf(10001))).thenReturn(results);
        List<Transaction> transactions =paymentServiceImpl.getAllTransaction(Long.valueOf(10001));
        assertEquals(transactions.get(0).getTransactionType(),TransactionTypeEnum.DEBIT );

    }

    @Test
    @DisplayName("Save new transaction")
    void saveTransaction() throws Exception {
     Transaction transaction = new Transaction();
     transaction.setAmount(Long.valueOf(100));
     transaction.setToAccount(Long.valueOf(10002));
     transaction.setFromAccount(Long.valueOf(10001));
     Account account = new Account();
     account.setBalance(Long.valueOf(200));
     account.setAccountNo(Long.valueOf(10001));
     Account account1 = new Account();
     account1.setBalance(Long.valueOf(200));
     account1.setAccountNo(Long.valueOf(10002));
     when(accountRepository.findById(Long.valueOf(10001))).thenReturn(account);
     when(accountRepository.findById(Long.valueOf(10002))).thenReturn(account1);
     when(transactionsRepository.saveTransaction(transaction)).thenReturn(Long.valueOf(100));
     Long transNumber =paymentServiceImpl.saveTransaction(transaction);
     assertEquals(transNumber, 100);

    }
}