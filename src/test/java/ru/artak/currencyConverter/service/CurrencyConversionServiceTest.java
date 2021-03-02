package ru.artak.currencyConverter.service;


import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.artak.currencyConverter.exception.ExchangeCodeNotFoundException;
import ru.artak.currencyConverter.exception.ExchangeServerNotFoundException;
import ru.artak.currencyConverter.rest.model.CurrencyConversionResponse;

import java.math.BigDecimal;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {CurrencyConversionServiceTest.Initializer.class})
public class CurrencyConversionServiceTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("exchanger")
            .withUsername("postgres")
            .withPassword("postgres");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.jdbc-url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @Test
    public void convert() throws ExchangeServerNotFoundException, ExchangeCodeNotFoundException {
        // given
        UUID userId = UUID.randomUUID();
        BigDecimal amountSource = new BigDecimal(1000);
        String currencyFrom = "RUB";
        String currencyTo = "USD";

        // when
        CurrencyConversionResponse actualResult =
                currencyConversionService.convert(userId, amountSource, currencyFrom, currencyTo);

        // then
        Assert.assertNotNull(actualResult);
    }
}