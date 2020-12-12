package ru.artak.currencyConverter.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artak.currencyConverter.dao.UserStatsDao;
import ru.artak.currencyConverter.dao.model.PopularDirectionData;
import ru.artak.currencyConverter.dao.model.UserStatsData;
import ru.artak.currencyConverter.rest.model.PopularDirectionResponse;
import ru.artak.currencyConverter.rest.model.UserStatsResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@Service
public class UserStatsService {
    private static final Logger logger = LogManager.getLogger(UserStatsService.class);

    @Autowired
    private final UserStatsDao userStatsDao;

    public UserStatsService(UserStatsDao userStatsDao) {
        this.userStatsDao = userStatsDao;
    }

    public List<UserStatsResponse> getAllStatsBySumByCode(BigDecimal currencyAmount, String currencyCode) {
        logger.debug("start statistics by {} , {}", currencyAmount, currencyCode);

        List<UserStatsData> allUsersBySumAmountByCurrencyAmount = userStatsDao.getAllUsersBySumAmountByCurrencyAmount(currencyAmount, currencyCode);
        return allUsersBySumAmountByCurrencyAmount.stream()
                .map(allUsersBySumAmountByCurrencyAmountData -> new UserStatsResponse(allUsersBySumAmountByCurrencyAmountData.getId()))
                .collect(toCollection(ArrayList::new));
    }

    public List<UserStatsResponse> getAllStatsByAmountAtAtime(BigDecimal currencyAmount, String currencyCode) {
        logger.debug("statistics by {} , {} at a time", currencyAmount, currencyCode);

        List<UserStatsData> usersStatsByAmountAtAtime = userStatsDao.getUsersStatsByAmountAtAtime(currencyAmount, currencyCode);
        return usersStatsByAmountAtAtime.stream()
                .map(usersStatsByAmountAtAtimeData -> new UserStatsResponse(usersStatsByAmountAtAtimeData.getId())).
                        collect(toCollection(ArrayList::new));
    }

    public List<PopularDirectionResponse> getPopularDestinationStats() {
        List<PopularDirectionData> popularDestination = userStatsDao.getPopularDestination();

        return popularDestination.stream()
                .map(popularDirectionData -> new PopularDirectionResponse(popularDirectionData.getTotalRequest(),
                        popularDirectionData.getCurrencyFrom(), popularDirectionData.getCurrencyTo()))
                .collect(toCollection(ArrayList::new));
    }
}
