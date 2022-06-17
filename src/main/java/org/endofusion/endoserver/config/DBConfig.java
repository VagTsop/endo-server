package org.endofusion.endoserver.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Profile("dev")
    @Bean
    public DataSource getDevDataSource() {
        HikariConfig config = new HikariConfig();
        HikariDataSource ds;
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        ds = new HikariDataSource(config);
        return ds;
    }
    @Profile("dev")
    @Bean
    public NamedParameterJdbcTemplate namedParamDevJdbcTemplate() {
        NamedParameterJdbcTemplate namedParamJdbcTemplate =
                new NamedParameterJdbcTemplate(getDevDataSource());
        return namedParamJdbcTemplate;
    }
    @Profile("prod")
    @Bean
    public DataSource getProdDataSource() {
        HikariConfig config = new HikariConfig();
        HikariDataSource ds;
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        ds = new HikariDataSource(config);//
        return ds;
    }
    @Profile("prod")
    @Bean
    public NamedParameterJdbcTemplate namedParamProdJdbcTemplate() {
        NamedParameterJdbcTemplate namedParamJdbcTemplate =
                new NamedParameterJdbcTemplate(getProdDataSource());
        return namedParamJdbcTemplate;
    }
}
