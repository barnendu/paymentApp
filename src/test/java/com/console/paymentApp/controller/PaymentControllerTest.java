package com.console.paymentApp.controller;

import com.console.paymentApp.entity.Account;
import com.console.paymentApp.exceptions.CustomException;
import com.console.paymentApp.model.PaymentAppResponse;
import com.console.paymentApp.model.Transaction;
import com.console.paymentApp.service.impl.PaymentServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PaymentControllerTest {

    PaymentServiceImpl paymentServiceImpl = Mockito.mock(PaymentServiceImpl.class);
    PaymentController paymentController;
    @BeforeEach
     void setup(){
        this.paymentController = new PaymentController(paymentServiceImpl);
    }
    @Test
    @DisplayName("Unit testing for no content scenario")
    void accounts_test_noContent() throws Exception {
        List<Account> accountList = new ArrayList<>();
        when(paymentServiceImpl.getAllAccount()).thenReturn(accountList);
        ResponseEntity<PaymentAppResponse> response = paymentController.accounts();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    @DisplayName("Unit testing for internal server error scenario")
    void accounts_test_internalServerError() throws Exception {
        List<Account> accountList = new ArrayList<>();
        when(paymentServiceImpl.getAllAccount()).thenThrow(new Exception("error."));
        ResponseEntity<PaymentAppResponse> response = paymentController.accounts();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    @DisplayName("Unit testing for data found scenario")
    void accounts_test_ok() throws Exception {
        List<Account> accountList = new ArrayList<>();
        Account account = new Account();
        account.setName("Test user");
        account.setBalance(Long.valueOf(500));
        accountList.add(account);
        when(paymentServiceImpl.getAllAccount()).thenReturn(accountList);
        ResponseEntity<PaymentAppResponse> response = paymentController.accounts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    @DisplayName("Unit testing for fetch all transaction bad request scenario")
    void transactions_test_badRequest() {
        ResponseEntity<PaymentAppResponse> response = paymentController.transactions(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    @DisplayName("Unit testing for fetch all transaction with no content scenario")
    void transactions_test_noContent() throws Exception {
        List<Transaction> transactionList = new ArrayList<>();
        when(paymentServiceImpl.getAllTransaction(Long.valueOf(1001))).thenReturn(transactionList);
        ResponseEntity<PaymentAppResponse> response = paymentController.transactions(Long.valueOf(1001));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    @DisplayName("Unit testing for fetch all transaction scenario")
    void transactions_test_OK() throws Exception {
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setFromAccount(Long.valueOf(10001));
        transaction.setToAccount(Long.valueOf(10002));
        transaction.setFromAccountName("Test Name");
        transactionList.add(transaction);
        when(paymentServiceImpl.getAllTransaction(Long.valueOf(1001))).thenReturn(transactionList);
        ResponseEntity<PaymentAppResponse> response = paymentController.transactions(Long.valueOf(1001));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Unit testing for fetch all transaction with internal server error scenario")
    void transactions_test_internalServer() throws Exception {
        List<Transaction> transactionList = new ArrayList<>();
        when(paymentServiceImpl.getAllTransaction(Long.valueOf(1001))).thenThrow(new Exception("test"));
        ResponseEntity<PaymentAppResponse> response = paymentController.transactions(Long.valueOf(1001));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    @DisplayName("Unit testing for transaction with bad request.")
    void transaction_badRequest(){
        Transaction transaction = new Transaction();
        ResponseEntity<PaymentAppResponse> response = paymentController.transaction(transaction);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Unit testing for transaction with ok response.")
    void transaction_OK() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(Long.valueOf(1001));
        transaction.setToAccount(Long.valueOf(1001));
        transaction.setAmount(Long.valueOf(1001));
        Long transNumber = Long.valueOf(1);
        when(paymentServiceImpl.saveTransaction(transaction)).thenReturn(transNumber);
        ResponseEntity<PaymentAppResponse> response = paymentController.transaction(transaction);
        assertEquals(response.getBody().getMessage(), "Transaction successful with transaction number: 1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    @DisplayName("Unit testing for transaction with invalid transaction ")
    void transaction_invalidTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(Long.valueOf(1001));
        transaction.setToAccount(Long.valueOf(1001));
        transaction.setAmount(Long.valueOf(1001));
        when(paymentServiceImpl.saveTransaction(transaction)).thenThrow(new CustomException("Invalid transaction"));
        ResponseEntity<PaymentAppResponse> response = paymentController.transaction(transaction);
        assertEquals(response.getBody().getMessage(), "Invalid transaction");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Unit testing for transaction with server error ")
    void transaction_internalServerError() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(Long.valueOf(1001));
        transaction.setToAccount(Long.valueOf(1001));
        transaction.setAmount(Long.valueOf(1001));
        when(paymentServiceImpl.saveTransaction(transaction)).thenThrow(new Exception("exception"));
        ResponseEntity<PaymentAppResponse> response = paymentController.transaction(transaction);
        assertEquals(response.getBody().getMessage(), "exception");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}