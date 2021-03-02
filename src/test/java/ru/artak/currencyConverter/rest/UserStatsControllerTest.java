package ru.artak.currencyConverter.rest;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.artak.currencyConverter.client.ExchangeRatesClient;
import ru.artak.currencyConverter.rest.model.UserStatsResponse;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class UserStatsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ExchangeRatesClient exchangeRatesClient;

    @Test
    public void getStats() throws IOException, InterruptedException {
        // given
//		Mockito.when(exchangeRatesClient.getExchangeRates()).thenReturn()

        // when
        // TODO в типе указать TypeReference
//		List<UserStatsResponse> response = restTemplate.getForEntity("/stats",  );

        // then
        // TODO пишешь asserts того, что вернулось правильно
    }
}