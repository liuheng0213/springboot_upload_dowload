package com.stephen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description
 * @Author sgl
 * @Date 2018-05-15 14:03
 */
@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan
@MapperScan("com.stephen.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
