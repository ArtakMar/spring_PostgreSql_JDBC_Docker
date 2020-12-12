package ru.artak.currencyConverter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserStatsResponse {
    private UUID id;
}
