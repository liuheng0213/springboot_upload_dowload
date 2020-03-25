package com.stephen.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatsSourceConfig {
    @Value("${spring.datasource.url}")
    public String url;

    @Value("${spring.datasource.password}")
    public String password;

    @Value("${spring.datasource.username}")
    public String username;

    @Value("${spring.datasource.driver-class-name}")
    public String driverClassName;

    @Value("${spring.datasource.min-idle}")
    public Integer minIdle;


    @Value("${spring.datasource.initial-size}")
    public Integer initialSize;


    @Value("${spring.datasource.max-active}")
    public Integer maxActive;


    @Value("${spring.datasource.max-wait}")
    public Integer maxWait;

    @Bean
    @Primary
    public DataSource druidDataSource() {
        System.out.println("==================d");
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        return datasource;
    }

}
