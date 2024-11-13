package com.openAi.application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.openAi")
public class OpenAiApplication {
	public static void main(String[] args) {
		SpringApplication.run(OpenAiApplication.class, args);
	}
}
