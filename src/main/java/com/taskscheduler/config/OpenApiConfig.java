package com.taskscheduler.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskSchedulerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Scheduler API")
                        .description("API for scheduling and managing automated tasks")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Task Scheduler Team")
                                .email("support@taskscheduler.com")
                                .url("https://taskscheduler.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}