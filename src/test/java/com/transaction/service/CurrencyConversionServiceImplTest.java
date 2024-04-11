package com.transaction.service;

import com.transaction.data.UnitTestData;
import com.transaction.model.ExchangeRateData;
import com.transaction.service.impl.CurrencyConversionServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyConversionServiceImplTest {


    private static final String baseUrl = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
    // get the exchange rate for a currency
    private static final String exchangeQueryUrl = "?fields=country_currency_desc,exchange_rate,record_date&filter=country_currency_desc:eq:{currency},record_date:gte:{startDate},record_date:lte:{endDate}&sort=-record_date";
    @InjectMocks
    private CurrencyConversionServiceImpl currencyConversionService;
    @Mock
    private WebClient webClientMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    static MockedStatic<WebClient> utilities;

    @Test
    public void getCurrencyConversionRateSuccess() {


        utilities.when(() -> WebClient.create(baseUrl)).thenReturn(webClientMock);
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);

        var date = LocalDate.now();
        var startYear = date.minusMonths(6).format(DateTimeFormatter.ISO_LOCAL_DATE);
        var endYear = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        var currency = "currency1";
        Mockito.when(requestHeadersUriSpecMock.uri(exchangeQueryUrl, currency, startYear, endYear))
                .thenReturn(requestHeadersSpecMock);


        ExchangeRateData data = new ExchangeRateData();
        data.setData(UnitTestData.getExchangeData());
        Mockito.when(responseSpecMock.bodyToMono((Class<ExchangeRateData>) Mockito.any()))
                .thenReturn(Mono.just(data));
        currencyConversionService.getCurrencyConversionRate(currency, date);


    }


    @Test
    public void getExchangeRate_nullResponse() throws Exception {


        utilities.when(() -> WebClient.create(baseUrl)).thenReturn(webClientMock);
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        var date = LocalDate.now();
        var startYear = date.minusMonths(6).format(DateTimeFormatter.ISO_LOCAL_DATE);
        var endYear = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        var currency = "currency1";
        Mockito.when(requestHeadersUriSpecMock.uri(exchangeQueryUrl, currency, startYear, endYear))
                .thenReturn(requestHeadersSpecMock);


        ExchangeRateData data = new ExchangeRateData();
        Mockito.when(responseSpecMock.bodyToMono((Class<ExchangeRateData>) Mockito.any()))
                .thenReturn(Mono.just(data));
        currencyConversionService.getCurrencyConversionRate(currency, date);

    }


    @BeforeAll
    public static void setupMocks() {
        utilities = Mockito.mockStatic(WebClient.class);


    }


}
