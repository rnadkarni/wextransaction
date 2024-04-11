package com.transaction.service;

import com.transaction.controller.TransactionController;
import com.transaction.data.UnitTestData;
import com.transaction.model.Transaction;
import com.transaction.repository.TransactionRepository;
import com.transaction.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionServiceImplTest {


    @InjectMocks
    private TransactionServiceImpl service;

    @Mock
    private TransactionRepository repository;

    @Test
    public void postTransactionTest()
    {
        Transaction tran= UnitTestData.getTransactionData();
        Mockito.when(repository.save(tran)).thenReturn(tran);
      service.postTransaction(tran.getDesc(), tran.getAmount(),tran.getDate());

    }


    @Test
    public void getTransactionByIdTest()
    {
        Transaction  tran=UnitTestData.getTransactionData();
        Optional<Transaction> transaction= Optional.of(tran);
        Mockito.when(repository.findById(1L)).thenReturn(transaction);
        service.getTransactionById(1L);

    }
}
