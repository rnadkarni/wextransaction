package com.transaction.controller;

import com.transaction.model.Transaction;
import com.transaction.service.CurrencyConversionService;
import com.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class TransactionController {


    @Autowired
    TransactionService service;


    @Autowired
    CurrencyConversionService currencyConversionService;

    @Value("${error.errorAmt}")
    private String errorAmt;
    @Value("${error.errorDesc}")
    private String errorDesc;

    @Value("${error.noTranasction}")
    private String errorNoTrans;

    @Value("${error.noExchange}")
    private String errorNoExchange;

    @Value("${error.internalServerError}")
    private String internalError;


    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private Logger logger = Logger.getLogger(TransactionController.class.getName());

    @PostMapping("/postTransaction")
    public ResponseEntity<Object> postTransaction(@RequestBody Transaction transaction) {

        logger.log(Level.INFO, "Posting Transaction");

        List<String> errorList = validateTransaction(transaction);

        if (errorList.size() > 0) {
            logger.log(Level.INFO, "Error in  Transaction");
            return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
        }
        try {
            Transaction tran = service.postTransaction(transaction.getDesc(), transaction.getAmount(), transaction.getDate());
            logger.log(Level.INFO, "Transaction Created");
            return new ResponseEntity<>(tran, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(internalError, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    private List<String> validateTransaction(Transaction transaction) {
        List<String> errorList = new ArrayList<>();
        logger.log(Level.INFO, "Validation Transaction");

        if (transaction.getDesc().length() > 50) {
            errorList.add(errorDesc);
        }
        if (transaction.getAmount() <= 0) {
            errorList.add(errorAmt);
        }

        return errorList;

    }


    @GetMapping("/transactions/{id}")
    public ResponseEntity<Object> getTransactionById(@PathVariable("id") long id, @RequestParam(required = false) String currency) {
        try {
            logger.log(Level.INFO, "Get Transaction by Id and exchange rate details");
            Optional<Transaction> transactionData = service.getTransactionById(id);

            if (transactionData.isEmpty()) {
                logger.log(Level.SEVERE, errorNoTrans);
                return new ResponseEntity<>(errorNoTrans, HttpStatus.NOT_FOUND);
            }

            Map<String, String> exchangeRateData = currencyConversionService.getCurrencyConversionRate(currency, transactionData.get().getDate());
            if (exchangeRateData == null || exchangeRateData.isEmpty()) {
                logger.log(Level.SEVERE, errorNoExchange);
                return new ResponseEntity<>(errorNoExchange, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> finalTransactionData = new LinkedHashMap<>();

            finalTransactionData.put("transaction_id", transactionData.get().getId());
            finalTransactionData.put("description", transactionData.get().getDesc());
            finalTransactionData.put("original_transaction_amount", transactionData.get().getAmount());
            finalTransactionData.put("transaction_date", transactionData.get().getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));


            var rate = Float.parseFloat(exchangeRateData.get("exchange_rate"));
            finalTransactionData.put("currency_amount", Float.parseFloat(decimalFormat.format(transactionData.get().getAmount() * rate)));
            finalTransactionData.put("currency", exchangeRateData.get("country_currency_desc"));
            finalTransactionData.put("exchange_rate", Float.valueOf(exchangeRateData.get("exchange_rate")));
            finalTransactionData.put("record_date", exchangeRateData.get("record_date"));
            logger.log(Level.INFO, "Displayed all details");
            return new ResponseEntity<>(finalTransactionData, HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(internalError, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
