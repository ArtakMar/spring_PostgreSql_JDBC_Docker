package ru.artak.currencyConverter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.artak.currencyConverter.rest.model.PopularDirectionResponse;
import ru.artak.currencyConverter.rest.model.UserStatsResponse;
import ru.artak.currencyConverter.service.UserStatsService;

import java.math.BigDecimal;
import java.util.List;

@RestController()
public class UserStatsController {

    @Autowired
    private final UserStatsService userStatsService;

    public UserStatsController(UserStatsService userStatsService) {
        this.userStatsService = userStatsService;
    }

    @GetMapping("/stats")
    public List<UserStatsResponse> getStats(@RequestParam(name = "currencyAmount") BigDecimal currencyAmount,
                                            @RequestParam(name = "currencyCode") String currencyCode,
                                            @RequestParam(name = "allExchange", required = false) Boolean allExchange) {

        if (allExchange == null) {
            return userStatsService.getAllStatsByAmountAtAtime(currencyAmount, currencyCode);
        }
        if (!allExchange) {
            return null;
        }
        return userStatsService.getAllStatsBySumByCode(currencyAmount, currencyCode);
    }

    @PostMapping("/stats")
    public List<PopularDirectionResponse> getPopularDestination(@RequestParam(name = "popularDestination") Boolean popularDestination) {
        if (!popularDestination) {
            return null;
        }
        return userStatsService.getPopularDestinationStats();
    }

}

