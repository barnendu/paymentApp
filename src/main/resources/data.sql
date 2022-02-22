create table account
(
    account_no integer not null AUTO_INCREMENT,
    name varchar(255) not null,
    email varchar(255) not null,
    balance integer not null,
    primary key(account_no)
);
INSERT INTO account (account_no, name, email, balance )
VALUES(10001,  'Ranga', 'barnendu.pal001@gmail.com', 200);
INSERT INTO account (account_no, name, email, balance )
VALUES(10002,  'Barnendu', 'pal.barnendu@gmail.com', 200);
INSERT INTO account (account_no, name, email, balance )
VALUES(10003,  'Andrei', 'andrei@email.com', 200);


create table transactions
(
    transaction_no integer not null AUTO_INCREMENT,
    from_account integer not null,
    to_account integer not null,
    amount integer not null,
    transaction_timestamp timestamp,
    primary key(transaction_no),
    FOREIGN KEY (from_account) REFERENCES account(account_no),
    FOREIGN KEY (to_account) REFERENCES account(account_no)
);

INSERT INTO transactions (transaction_no, from_account, to_account,  amount, transaction_timestamp)
VALUES(1000001,  10001, 10002,  20, sysdate());
INSERT INTO transactions (transaction_no, from_account, to_account,  amount, transaction_timestamp)
VALUES(1000002,  10001, 10003,  10, sysdate());
INSERT INTO transactions (transaction_no, from_account, to_account,  amount, transaction_timestamp)
VALUES(1000003,  10002, 10001,  5, sysdate());