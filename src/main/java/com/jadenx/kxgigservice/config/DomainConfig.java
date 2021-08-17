package com.jadenx.kxgigservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.jadenx.kxgigservice.domain")
@EnableJpaRepositories("com.jadenx.kxgigservice.repos")
@EnableTransactionManagement
public class DomainConfig {
}
