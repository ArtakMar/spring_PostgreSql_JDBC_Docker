package ru.artak.currencyConverter.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PopularDirectionData {
    int totalRequest;
    String currencyFrom;
    String currencyTo;

}
