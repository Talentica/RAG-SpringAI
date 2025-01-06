package com.openAi.security.utils;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class JpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource,
            JpaProperties jpaProperties) {
        return builder
                .dataSource(dataSource)
                .packages("com.openAi.security.entity")
                .persistenceUnit("default")
                .properties(jpaProperties.getProperties())
                .build();
    }
}
