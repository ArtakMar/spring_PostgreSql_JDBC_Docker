package ru.artak.currencyConverter.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void saveUser(UUID userId) {
        String sqlForUser = "INSERT INTO users (id) VALUES (:userId) ON CONFLICT(id) DO NOTHING";

        MapSqlParameterSource paramsForUser = new MapSqlParameterSource();
        paramsForUser.addValue("userId", userId);

        namedParameterJdbcTemplate.update(sqlForUser, paramsForUser);
    }
}

