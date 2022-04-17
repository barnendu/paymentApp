package com.console.paymentApp.service.impl;

import com.console.paymentApp.entity.Account;
import com.console.paymentApp.entity.Transactions;
import com.console.paymentApp.enums.TransactionTypeEnum;
import com.console.paymentApp.exceptions.CustomException;
import com.console.paymentApp.model.Transaction;
import com.console.paymentApp.repository.AccountRepository;
import com.console.paymentApp.repository.TransactionsRepository;
import com.console.paymentApp.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component("PaymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    AccountRepository accountRepository;
    TransactionsRepository transactionsRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public PaymentServiceImpl(AccountRepository accountRepository, TransactionsRepository transactionsRepository) {
        this.accountRepository = accountRepository;
        this.transactionsRepository = transactionsRepository;
    }
    @Override
    public List<Account> getAllAccount() throws Exception {
        return accountRepository.fetchAllAccounts();
    }

    @Override
    public List<Transaction> getAllTransaction(Long account) throws Exception {
        List<Transaction> transactionList = new ArrayList<>();

        try {
            List<Object[]> results = transactionsRepository.fetchAllTransaction(account);
            for (Object[] result : results) {
                Transaction transaction = new Transaction();
                transaction.setTransactionNo((Long) result[0]);
                transaction.setFromAccount((Long) result[1]);
                transaction.setToAccount((Long) result[2]);
                transaction.setAmount((Long) result[3]);
                transaction.setTransactionTimeStamp((LocalDateTime)result[4]);
                transaction.setFromAccountName((String) result[5]);
                transaction.setToAccountName((String) result[6]);
                if( account.equals((Long) result[1])){
                    transaction.setTransactionType(TransactionTypeEnum.DEBIT);
                }
                else {
                    transaction.setTransactionType(TransactionTypeEnum.CREDIT);
                }
                transactionList.add(transaction);
            }
            return transactionList;
        }
        catch(Exception ex){
            logger.error("Error while processing this request: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Long saveTransaction(Transaction transactionReq) throws Exception {
        Long transactionNumber = null;
        try {
            Account formAccount = accountRepository.findById(transactionReq.getFromAccount());
            if (formAccount.getBalance() >= transactionReq.getAmount()) {
                transactionNumber = transactionsRepository.saveTransaction(transactionReq);
                if (transactionNumber != null) {
                    formAccount.setBalance(formAccount.getBalance() - transactionReq.getAmount());
                    accountRepository.updateAccount(formAccount);
                    Account toAccount = accountRepository.findById(transactionReq.getToAccount());
                    toAccount.setBalance(toAccount.getBalance() + transactionReq.getAmount());
                    accountRepository.updateAccount(toAccount);
                    // sendEmail(formAccount.getEmail(), "debited", transactionReq.getAmount());
                    //sendEmail(toAccount.getEmail(), "credited", transactionReq.getAmount());
                }
            } else {
                throw new CustomException("Invalid transaction");
            }
        }catch (Exception ex){
            logger.error("Error while processing this request: {}", ex.getMessage());
            throw ex;
        }
      return transactionNumber;
    }


    private void sendEmail(String to_mail, String transactionType, Long amount) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(to_mail);
        helper.setSubject("Payment application notification");
        String content = "<h3>Your account has been " + transactionType + " with " + amount +" pound </h3>";
        helper.setText(content, true);
        javaMailSender.send(msg);

    }
}
