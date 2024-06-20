package com.pluralsight.dealership.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    private BasicDataSource basicDataSource;

    @Bean
    public DataSource dataSource(){
        return basicDataSource;
    }

    @Autowired
    public DatabaseConfig(@Value("${spring.datasource.url}") String url){
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(url);
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("password");
    }
}
