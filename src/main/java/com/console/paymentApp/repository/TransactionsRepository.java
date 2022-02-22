package com.console.paymentApp.repository;

import com.console.paymentApp.entity.Transactions;
import com.console.paymentApp.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class TransactionsRepository {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;


    public List<Object[]> fetchAllTransaction(Long account){
        String query = "select tran.transactionNo,\n" +
                "      tran.fromAccount,\n" +
                "      tran.toAccount,\n" +
                "      tran.amount,\n" +
                "      tran.transactionTimestamp,\n" +
                "      from_acc.name as fromAccountName,\n" +
                "      to_acc.name as toAccountName\n" +
                "from Transactions tran\n" +
                "inner join Account from_acc\n" +
                "on tran.fromAccount = from_acc.accountNo and (" +
                "tran.fromAccount=:frmAcc or tran.toAccount=:toAcc ) \n" +
                "inner join Account to_acc on tran.toAccount = to_acc.accountNo";
        return entityManager.createQuery(query).setParameter("frmAcc", account)
                .setParameter("toAcc", account).getResultList();
    }

    public Long saveTransaction(Transaction transactionReq){
            Transactions transactions = new Transactions(transactionReq.getFromAccount(),
                    transactionReq.getToAccount(),
                    transactionReq.getAmount());
            entityManager.persist(transactions);
            entityManager.flush();
            return transactions.getTransactionNo();
    }
}
