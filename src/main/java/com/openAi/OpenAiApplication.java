package com.openAi;


import com.openAi.security.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@RequiredArgsConstructor
public class OpenAiApplication {

	private final AuthenticationFilter filter;

	public static void main(String[] args) {
		SpringApplication.run(OpenAiApplication.class, args);
	}
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}
}
