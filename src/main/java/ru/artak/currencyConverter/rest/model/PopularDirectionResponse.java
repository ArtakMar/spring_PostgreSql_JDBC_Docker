package ru.artak.currencyConverter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularDirectionResponse {
    int totalRequest;
    String currencyFrom;
    String currencyTo;
}
