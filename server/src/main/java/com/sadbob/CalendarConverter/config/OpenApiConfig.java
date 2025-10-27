package com.sadbob.CalendarConverter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:Calendar Converter API}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Calendar Converter API")
                        .description("""
                    REST API for converting dates between Gregorian, Ethiopian, and Hijri calendars.
                    
                    ## Features:
                    - Single and bulk date conversion
                    - Monthly calendar views for all supported calendars
                    - Holiday information and checking
                    - Age calculation across different calendars
                    - Export functionality (PDF, ICS, JSON, CSV)
                    - Bulk conversion with async support
                    """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Calendar Converter Support")
                                .email("support@sadbob.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:8585").description("Development Server"),
                        new Server().url("https://your-production-url.com").description("Production Server")
                ));
    }
}
