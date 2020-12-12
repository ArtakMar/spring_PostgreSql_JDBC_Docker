package ru.artak.currencyConverter.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {
    public ExchangeRate() {
    }

    @JsonProperty("rates")
    private final HashMap<String, BigDecimal> rates = new HashMap<>();
}
