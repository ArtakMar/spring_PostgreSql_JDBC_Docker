package ru.artak.currencyConverter.rest.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CurrencyConversionResponse {
    private UUID requestId;
    private BigDecimal targetAmount;

}
