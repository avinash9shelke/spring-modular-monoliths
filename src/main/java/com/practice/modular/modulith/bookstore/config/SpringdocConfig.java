package com.practice.modular.modulith.bookstore.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author avinash
 */
@Configuration
public class SpringdocConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Bookstore API")
                        .description("Spring Bookstore sample application")
                        .version("v0.0.1")
                        .license(new License().name("MIT").url("https://modulithview.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Bookstore  Documentation")
                        .url("https://modulithview.com"));
    }
}
