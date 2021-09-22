package com.jadenx.kxgigservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class KxGigServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(KxGigServiceApplication.class, args);
    }

}
