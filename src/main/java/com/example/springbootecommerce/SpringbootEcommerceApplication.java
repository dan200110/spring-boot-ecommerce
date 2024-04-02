package com.example.springbootecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringbootEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootEcommerceApplication.class, args);
    }

}
