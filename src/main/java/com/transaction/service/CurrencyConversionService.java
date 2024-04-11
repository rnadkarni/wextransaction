package com.transaction.service;

import java.time.LocalDate;
import java.util.Map;

public interface CurrencyConversionService {

    public Map<String, String> getCurrencyConversionRate(String currency, LocalDate date);
}
