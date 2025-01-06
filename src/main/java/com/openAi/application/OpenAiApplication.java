package com.openAi.application;


import com.openAi.security.AuthenticationFilter;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
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
	@Bean
	ChatModel chatModel(@Value("${spring.ai.openai.api-key}") String apiKey) {
		return new OpenAiChatModel(new OpenAiApi(apiKey));
	}
}
