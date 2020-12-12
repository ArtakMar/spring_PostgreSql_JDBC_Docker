package ru.artak.currencyConverter.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artak.currencyConverter.client.ExchangeRatesClient;
import ru.artak.currencyConverter.dao.CurrencyConversionDao;
import ru.artak.currencyConverter.dao.UserDao;
import ru.artak.currencyConverter.exception.ExchangeCodeNotFoundException;
import ru.artak.currencyConverter.exception.ExchangeServerNotFoundException;
import ru.artak.currencyConverter.rest.model.CurrencyConversionResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

@Service
public class CurrencyConversionService {
    private static final Logger logger = LogManager.getLogger(CurrencyConversionService.class);
    @Autowired
    private final ExchangeRatesClient exchangeRatesClient;
    @Autowired
    private final CacheManager cacheManager;
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final CurrencyConversionDao currencyConversionDao;


    public CurrencyConversionService(ExchangeRatesClient exchangeRatesClient, CacheManager cacheManager,
                                     UserDao userDao, CurrencyConversionDao currencyConversionDao) {
        this.exchangeRatesClient = exchangeRatesClient;
        this.cacheManager = cacheManager;
        this.userDao = userDao;
        this.currencyConversionDao = currencyConversionDao;
    }

    @Transactional
    public CurrencyConversionResponse convert(UUID userId, BigDecimal amountSource, String currencySource, String currencyTarget)
            throws ExchangeServerNotFoundException, ExchangeCodeNotFoundException {

        final UUID requestId = UUID.randomUUID();
        saveExchangeRatesInCache();
        checkClientRequest(userId, amountSource, currencySource, currencyTarget);
        userDao.saveUser(userId);
        currencyConversionDao.saveExchange(userId, requestId, amountSource, currencySource, currencyTarget);

        return getResponseExchange(requestId, amountSource, currencySource, currencyTarget);
    }

    private void checkClientRequest(UUID userId, BigDecimal sourceAmount, String currencySource, String currencyTarget) throws ExchangeCodeNotFoundException {
        if (sourceAmount.signum() <= 0 || Objects.requireNonNull(cacheManager.getCache("exchangeRateCache")).get(currencySource) == null ||
                Objects.requireNonNull(cacheManager.getCache("exchangeRateCache")).get(currencyTarget) == null) {
            logger.warn("bad request user - {} sourceAmount - {} currencySource - {}  currencyTarget - {}",
                    userId, sourceAmount, currencySource, currencyTarget);
            throw new ExchangeCodeNotFoundException("invalid request format, please try again. " +
                    "Example: exchange?user_id=33&amountFrom=2.2&currencySource=USD&currencyTarget=RUB");
        }
    }

    public void saveExchangeRatesInCache() throws ExchangeServerNotFoundException {
        logger.debug("save exchange rates in cache");
        try {
            exchangeRatesClient.getExchangeRates();
        } catch (IOException | InterruptedException e) {
            logger.warn("exchange server not available", e);
            throw new ExchangeServerNotFoundException("exchange server not available");
        }
    }

    private CurrencyConversionResponse getResponseExchange(UUID requestId, BigDecimal sourceAmount, String currencySource, String currencyTarget) {
        if (currencySource.equals(currencyTarget)) {
            return new CurrencyConversionResponse(requestId, sourceAmount);
        }
        Cache.ValueWrapper currencyBaseToSourceElement = Objects.requireNonNull(cacheManager.getCache("exchangeRateCache")).get(currencySource);
        assert currencyBaseToSourceElement != null;
        BigDecimal amountBaseToSource = (BigDecimal) currencyBaseToSourceElement.get();

        Cache.ValueWrapper currencyBaseToTarget = Objects.requireNonNull(cacheManager.getCache("exchangeRateCache")).get(currencyTarget);
        assert currencyBaseToTarget != null;
        BigDecimal amountBaseToTarget = (BigDecimal) currencyBaseToTarget.get();

        BigDecimal amountTarget = (sourceAmount.multiply(amountBaseToTarget)).divide(amountBaseToSource, 2, RoundingMode.HALF_UP);
        return new CurrencyConversionResponse(requestId, amountTarget);
    }
}
