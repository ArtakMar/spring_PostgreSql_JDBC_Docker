package ru.artak.currencyConverter.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class JpaConfig extends HikariConfig {

    @Bean
    public HikariDataSource getHikariDataSource() {
        return new HikariDataSource(this);
    }
}
