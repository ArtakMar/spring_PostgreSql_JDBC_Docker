package ru.artak.currencyConverter.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.artak.currencyConverter.dao.model.PopularDirectionData;
import ru.artak.currencyConverter.dao.model.UserStatsData;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public class UserStatsDao {
    private static final Logger logger = LogManager.getLogger(UserStatsDao.class);

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserStatsDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<UserStatsData> getAllUsersBySumAmountByCurrencyAmount(BigDecimal currencyAmount, String currencyCode) {
        String sqlForUserStats = "SELECT u.id, sum(amount_source) as total " +
                "FROM users as u INNER JOIN currency_conversions cc on u.id = cc.user_id and cc.currency_source = :currencyCode " +
                "group by u.id " +
                "HAVING sum(amount_source) > :currencyAmount";

        MapSqlParameterSource paramForUserStats = new MapSqlParameterSource();
        paramForUserStats.addValue("currencyCode", currencyCode);
        paramForUserStats.addValue("currencyAmount", currencyAmount);
        logger.info("completed successfully request - users who have the total requested currencyAmount :{} and currencyCode : {}",
                currencyAmount, currencyCode);
        return namedParameterJdbcTemplate.query(sqlForUserStats, paramForUserStats, rowMapper);
    }

    private final RowMapper<UserStatsData> rowMapper = (rs, rowNum) -> {
        UUID id = UUID.fromString(rs.getString("id"));
        return new UserStatsData(id);
    };

    public List<UserStatsData> getUsersStatsByAmountAtAtime(BigDecimal currencyAmount, String currencyCode) {
        String sqlStatsAtAtime = "SELECT u.id " +
                "FROM users as u INNER JOIN currency_conversions cc on u.id = cc.user_id " +
                "WHERE cc.currency_source = :currencyCode AND cc.amount_source >:currencyAmount " +
                "group by u.id;";

        MapSqlParameterSource paramStatsAtAtime = new MapSqlParameterSource();
        paramStatsAtAtime.addValue("currencyCode", currencyCode);
        paramStatsAtAtime.addValue("currencyAmount", currencyAmount);
        logger.info("completed successfully request - change at a time by currencyAmount: {} , currencyCode: {}", currencyAmount, currencyCode);
        return namedParameterJdbcTemplate.query(sqlStatsAtAtime, paramStatsAtAtime, rowMapper);
    }

    public List<PopularDirectionData> getPopularDestination() {
        String splPopularDestination = "select count(id), currency_source, currency_target " +
                "FROM currency_conversions " +
                "group by currency_source, currency_target " +
                "order by count(id) DESC";

        MapSqlParameterSource paramPopular = new MapSqlParameterSource();
        logger.info("completed successfully request - popularDestination");

        return namedParameterJdbcTemplate.query(splPopularDestination, paramPopular, rowMapperForPopularDirection);
    }

    private final RowMapper<PopularDirectionData> rowMapperForPopularDirection = (rs, rowNum) -> {
        int totalRequest = rs.getInt(1);
        String currencyFrom = rs.getString(2);
        String currencyTo = rs.getString(3);

        return new PopularDirectionData(totalRequest, currencyFrom, currencyTo);
    };
}

