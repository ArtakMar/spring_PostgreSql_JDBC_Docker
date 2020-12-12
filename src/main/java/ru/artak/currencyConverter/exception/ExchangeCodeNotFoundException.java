package ru.artak.currencyConverter.exception;

public class ExchangeCodeNotFoundException extends Exception {
    public ExchangeCodeNotFoundException(String message) {
        super(message);
    }
}
