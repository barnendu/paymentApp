package com.console.paymentApp.entity;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long transactionNo;

    @Column(nullable = false)
    private Long fromAccount;

    @Column(nullable = false)
    private Long toAccount;

    @Column(nullable = false)
    private Long amount;

    @CreationTimestamp
    private LocalDateTime transactionTimestamp;

    public Transactions() {
    }

    public Transactions(Long fromAccount, Long toAccount, Long amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public Long getTransactionNo() {
        return transactionNo;
    }

    public Long getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public void setToAccount(Long toAccount) {
        this.toAccount = toAccount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionTimestamp() {
        return transactionTimestamp;
    }

}
