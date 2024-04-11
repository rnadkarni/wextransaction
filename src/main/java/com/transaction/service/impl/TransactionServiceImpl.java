package com.transaction.service.impl;

import com.transaction.model.Transaction;
import com.transaction.repository.TransactionRepository;
import com.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction postTransaction(String desc, float amount, LocalDate date) {
        Transaction t = transactionRepository.save(new Transaction(desc, amount, date));
        return t;
    }


    @Override
    public Optional<Transaction> getTransactionById(long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction;
    }
}
