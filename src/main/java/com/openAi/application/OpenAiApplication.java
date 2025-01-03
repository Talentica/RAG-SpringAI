package com.openAi.application;


import com.openAi.security.AuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.openAi")
@EnableJpaRepositories(basePackages = "com.openAi.security.repository")
@EntityScan(basePackages = "com.openAi.security.entity")
public class OpenAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiApplication.class, args);
	}

}
