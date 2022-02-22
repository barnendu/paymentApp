package com.console.paymentApp.repository;

//import com.console.paymentApp.model.Account;
import com.console.paymentApp.entity.Account;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.console.paymentApp.entity.Transactions;
import com.console.paymentApp.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AccountRepository {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    public List<Account> fetchAllAccounts(){
        return entityManager.createQuery("select a from Account a").getResultList();
    }
    public Account findById(Long accountNo){
        return entityManager.find(Account.class, accountNo);
    }
    public void updateAccount(Account account){
        entityManager.merge(account);
    }

}
