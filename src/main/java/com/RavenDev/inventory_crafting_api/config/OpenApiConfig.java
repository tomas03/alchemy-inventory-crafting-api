package com.RavenDev.inventory_crafting_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory & Crafting API")
                        .version("1.0.0")
                        .description("API REST para gestión de inventarios con apilado (stacking), recetas y transmutación/crafteo transaccional.")
                        .contact(new Contact()
                                .name("Tomas Fantinel")
                                .url("https://github.com/tomas03")));
    }
}