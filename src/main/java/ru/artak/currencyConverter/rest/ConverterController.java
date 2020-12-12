package ru.artak.currencyConverter.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artak.currencyConverter.exception.ExchangeCodeNotFoundException;
import ru.artak.currencyConverter.exception.ExchangeServerNotFoundException;
import ru.artak.currencyConverter.rest.model.CurrencyConversionResponse;
import ru.artak.currencyConverter.rest.model.ErrorResponse;
import ru.artak.currencyConverter.service.CurrencyConversionService;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
public class ConverterController {
    private final CurrencyConversionService currencyConversionService;

    public ConverterController(CurrencyConversionService currencyConversionService) {
        this.currencyConversionService = currencyConversionService;
    }

    @GetMapping("/exchange")
    public CurrencyConversionResponse convert(@RequestParam(name = "userId") UUID userId,
                                              @RequestParam(name = "currencySource") String currencySource,
                                              @RequestParam(name = "amountSource") BigDecimal amountSource,
                                              @RequestParam(name = "currencyTarget") String currencyTarget)
            throws ExchangeServerNotFoundException, ExchangeCodeNotFoundException {
        return currencyConversionService.convert(userId, amountSource, currencySource, currencyTarget);
    }

    @ExceptionHandler(ExchangeServerNotFoundException.class)
    public ResponseEntity<ErrorResponse> exchangeServerNotFoundException(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.SERVICE_UNAVAILABLE.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ExchangeCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> exchangeCodeNotFoundException(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
