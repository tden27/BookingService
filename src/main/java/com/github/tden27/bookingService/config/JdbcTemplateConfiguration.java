package com.github.tden27.bookingService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableTransactionManagement
@ComponentScan
public class JdbcTemplateConfiguration {
    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource("jdbc:postgresql://localhost:5432/booking_source", "postgres", "password");
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @PostConstruct
    public void makeScript() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource().getConnection(), new ClassPathResource("/schema.sql"));
    }
}
