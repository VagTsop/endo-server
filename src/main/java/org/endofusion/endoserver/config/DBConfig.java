package org.endofusion.endoserver.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Bean
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        HikariDataSource ds;
     // config.setJdbcUrl("jdbc:mysql://localhost:3306/endo?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        config.setJdbcUrl("postgres://otijcczdwueera:d187dba3ff481ebf5dd93e9d55d8b5e01b51df2cd30eca0f97998b4e7c56d741@ec2-52-73-184-24.compute-1.amazonaws.com:5432/ddc63hifqf59ok");
        config.setUsername("root");
        config.setPassword("root");
        ds = new HikariDataSource(config);
        return ds;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParamJdbcTemplate() {
        NamedParameterJdbcTemplate namedParamJdbcTemplate =
                new NamedParameterJdbcTemplate(getDataSource());
        return namedParamJdbcTemplate;
    }
}
