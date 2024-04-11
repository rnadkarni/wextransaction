package com.transaction.controller;


import com.transaction.data.UnitTestData;
import com.transaction.model.Transaction;
import com.transaction.service.CurrencyConversionService;
import com.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {


    @InjectMocks
    private TransactionController controller;

    @Mock
    private TransactionService service;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Test
    public void exchangeSuccess()
    {
        Transaction  tran=UnitTestData.getTransactionData();
        Optional<Transaction> transaction= Optional.of(tran);
        var exchangeRateData = new HashMap<String, String>();
        exchangeRateData.put("currency", UnitTestData.currency);
        exchangeRateData.put("exchange_rate", "1.3");
        exchangeRateData.put("record_date", "2024-01-01");

        Mockito.when( currencyConversionService.getCurrencyConversionRate(UnitTestData.currency, transaction.get().getDate())).thenReturn(exchangeRateData);
        Mockito.when(service.getTransactionById(tran.getId())).thenReturn(transaction);
        controller.getTransactionById(tran.getId(),UnitTestData.currency);
    }

    @Test
    public void emptyTransaction()
    {
        Transaction  tran=UnitTestData.getTransactionData();
        Optional<Transaction> transaction= Optional.empty();
        Mockito.when(service.getTransactionById(tran.getId())).thenReturn(transaction);
        controller.getTransactionById(tran.getId(),UnitTestData.currency);
    }


    @Test
    public void emptyExchangeRate()
    {
        Transaction  tran=UnitTestData.getTransactionData();
        Optional<Transaction> transaction= Optional.of(tran);
        Mockito.when( currencyConversionService.getCurrencyConversionRate(UnitTestData.currency, transaction.get().getDate())).thenReturn(new HashMap<String, String>());
        Mockito.when(service.getTransactionById(tran.getId())).thenReturn(transaction);
        controller.getTransactionById(tran.getId(),UnitTestData.currency);
    }



    @Test
    public void postTransaction() {

        Transaction  tran=UnitTestData.getTransactionData();
        Mockito.when(service.postTransaction(tran.getDesc(), tran.getAmount(), tran.getDate())).thenReturn(tran);
        controller.postTransaction(tran);
    }

    @Test
    public void postTransactionError() {

        Transaction  tran=UnitTestData.getNegativeTransactionData();
        Mockito.when(service.postTransaction(tran.getDesc(), tran.getAmount(), tran.getDate())).thenReturn(tran);
        controller.postTransaction(tran);
    }
}
