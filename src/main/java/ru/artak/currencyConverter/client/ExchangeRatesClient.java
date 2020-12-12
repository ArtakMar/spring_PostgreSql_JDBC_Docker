package ru.artak.currencyConverter.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.artak.currencyConverter.client.model.ExchangeRate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;


@Component
public class ExchangeRatesClient {
    private static final Logger logger = LogManager.getLogger(ExchangeRatesClient.class);
    private final String OPEN_EXCHANGE_RATES_BASE_URL;
    private final ObjectMapper mapper = new ObjectMapper();
    private final CacheManager cacheManager;
    private final HttpClient httpClientForOpenApiExchange = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

    public ExchangeRatesClient(@Value("${exchange.url}") String open_exchange_rates_base_url, CacheManager cacheManager) {
        OPEN_EXCHANGE_RATES_BASE_URL = open_exchange_rates_base_url;
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = "exchangeRateCache")
    public void getExchangeRates() throws IOException, InterruptedException {
        HttpRequest requestExchangeRates = HttpRequest.newBuilder()
                .uri(URI.create(OPEN_EXCHANGE_RATES_BASE_URL))
                .GET()
                .build();
        HttpResponse<String> exchangeResponse = httpClientForOpenApiExchange.send(requestExchangeRates, HttpResponse.BodyHandlers.ofString());

        ExchangeRate exchangeRates = mapper.readValue(exchangeResponse.body(), new TypeReference<>() {
        });

        saveInCache(exchangeRates);
    }

    private void saveInCache(ExchangeRate exchangeRates) {
        HashMap<String, BigDecimal> rates = exchangeRates.getRates();
        rates.forEach((key, value) -> Objects.requireNonNull(cacheManager.getCache("exchangeRateCache")).put(key, value));
        logger.info("Save ExchangeRate in Cache - {} ", exchangeRates.getRates());
    }
}
