package de.onpier.assignment.config;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class SpringDocConfig {

	private final Environment env;

	@Bean
	public GroupedOpenApi creditPurchaseApi() {
		return GroupedOpenApi.builder().group("anpier").packagesToScan("de.onpier.assignment.api.web").build();
	}

}
