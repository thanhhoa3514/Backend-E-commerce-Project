package com.project.e_commerce.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce Application API",
                version = "1.0",
                description = "This API exposes endpoints for the E-commerce application.",
                contact = @Contact(
                        name = "E-commerce API Support",
                        email = "support@ecommerce.com",
                        url = "https://www.ecommerce.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://choosealicense.com/licenses/mit/"
                ),
                termsOfService = "https://www.ecommerce.com/terms"
        ),
        servers = {
                @Server(url = "http://localhost:8088", description = "Local Development Server"),
                @Server(url = "http://45.117.179.16:8088", description = "Production Server")
        },
        security = {
                @SecurityRequirement(name = "bearer-key")
        }
)
@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    
}
