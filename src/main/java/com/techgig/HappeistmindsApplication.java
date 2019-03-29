package com.techgig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HappeistmindsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HappeistmindsApplication.class, args);
	}

	@Bean
	@Scope(value = "prototype")
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
