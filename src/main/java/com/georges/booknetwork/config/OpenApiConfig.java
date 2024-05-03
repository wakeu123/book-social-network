package com.georges.booknetwork.config;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "ING. Wakeu georges favier",
                        email = "georgeswakeu21@gmail.com",
                        url = "georges-courses.com"
                ),
                description = "Book network api",
                title = "Book network api",
                version = "1.0.0",
                license = @License(
                        name = "Licence name",
                        url = "https://licences.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8088/api/v1"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "https://book-network-api.com"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = HTTP,
        bearerFormat = "JWT",
        in = HEADER
)
public class OpenApiConfig {
}
