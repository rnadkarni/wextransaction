package com.transaction.service.impl;

import com.transaction.model.ExchangeRateData;
import com.transaction.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String fiscalUrl = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
    private static final String exchangeRateUrl = "?fields=country_currency_desc,exchange_rate,record_date&filter=country_currency_desc:eq:{currency},record_date:gte:{startDate},record_date:lte:{endDate}&sort=-record_date";
    private Logger logger = Logger.getLogger(CurrencyConversionServiceImpl.class.getName());
    private WebClient webClient = WebClient.create(fiscalUrl);

    @Override
    public Map<String, String> getCurrencyConversionRate(String currency, LocalDate date) {
        var startYear = date.minusMonths(6).format(DATE_FORMAT);
        var endYear = date.format(DATE_FORMAT);
        logger.log(Level.SEVERE, "Call exchange date before 6 month before the purchased date");
        var responseData = webClient.get().uri(exchangeRateUrl, currency, startYear, endYear).retrieve();

        Mono<ExchangeRateData> responseBody = responseData.bodyToMono(ExchangeRateData.class);
        var exchangeRates = responseBody.block().getData();
        if (exchangeRates == null || exchangeRates.isEmpty()) {
            logger.log(Level.SEVERE, "Exchange rate not found");
            return null;
        }
        logger.log(Level.SEVERE, "Get Latest exchange rate");

        return exchangeRates.get(0);
    }
}
