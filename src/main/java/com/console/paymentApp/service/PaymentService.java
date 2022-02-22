package com.console.paymentApp.service;

import com.console.paymentApp.entity.Account;
import com.console.paymentApp.entity.Transactions;
import com.console.paymentApp.exceptions.CustomException;
import com.console.paymentApp.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {

    public  List<Account> getAllAccount() throws Exception;
    public  List<Transaction> getAllTransaction(Long account) throws Exception;
    public Long saveTransaction(Transaction transactionReq) throws CustomException, Exception;
}
