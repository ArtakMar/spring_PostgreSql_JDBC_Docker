package ru.artak.currencyConverter.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class CurrencyConversionDao {

    private static final Logger logger = LogManager.getLogger(CurrencyConversionDao.class);
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CurrencyConversionDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public void saveExchange(UUID userId, UUID requestId, BigDecimal amountSource, String currencySource, String currencyTarget) {
        String sqpForConversion = "INSERT INTO currency_conversions (user_id, request_id, amount_source, currency_source, currency_target) " +
                "VALUES (:user_id, :request_id, :amountSource, :currencySource, :currencyTarget)";

        MapSqlParameterSource paramsForConversion = new MapSqlParameterSource();
        paramsForConversion.addValue("request_id", requestId);
        paramsForConversion.addValue("amountSource", amountSource);
        paramsForConversion.addValue("currencySource", currencySource);
        paramsForConversion.addValue("currencyTarget", currencyTarget);
        paramsForConversion.addValue("user_id", userId);

        namedParameterJdbcTemplate.update(sqpForConversion, paramsForConversion);
        logger.info("saved user and exchange request: user - {}, request_id - {}, amountSource - {}, currencySource - {}, currencyTarget - {}",
                userId, requestId, amountSource, currencySource, currencyTarget);
    }
}
