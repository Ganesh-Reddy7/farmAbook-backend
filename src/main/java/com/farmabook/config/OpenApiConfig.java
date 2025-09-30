package com.farmabook.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI farmAbookApi() {
        // Define JWT Bearer security scheme
        SecurityScheme bearerAuthScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("FarmAbook API")
                        .description("API documentation for managing Farmers, Investments, Workers, and Returns")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ganesh Reddy")
                                .email("ganeshreddym4174@gmail.com")
                                .url("https://ganeshkumarreddy.netlify.app"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                // Add JWT scheme to components
                .components(new Components().addSecuritySchemes("bearerAuth", bearerAuthScheme))
                // Apply security globally (all endpoints except login/register)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
