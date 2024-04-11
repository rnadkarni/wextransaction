package com.transaction.data;

import com.transaction.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class UnitTestData {

    public static String currency="ALBANIA-LEK";
  public static  LocalDate date = LocalDate.of(2020, 1, 8);

    public static Transaction getTransactionData()
    {
        int id=1;
        Transaction tran= new Transaction();
        tran.setId(1);
        tran.setDesc("trade amount of 100.126");
        tran.setDate(date);
        tran.setAmount(100.123f);
        return  tran;
    }

    public static Transaction getNegativeTransactionData()
    {
        Transaction tran= new Transaction();

        tran.setDate(date);
        tran.setDesc("trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126trade amount of 100.126");
        tran.setAmount(-1.3f);
        return tran;
    }

    public static List<Map<String, String>> getExchangeData() {
        List<Map<String, String>> currencies = new ArrayList<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("currency", "UK");
        currencies.add(currency);

        return currencies;
    }
}
