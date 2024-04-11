package com.transaction.service;

import com.transaction.model.Transaction;

import java.time.LocalDate;
import java.util.Optional;

public interface TransactionService {


    public Transaction postTransaction(String desc, float amount, LocalDate date);

    public Optional<Transaction> getTransactionById(long id);

}
