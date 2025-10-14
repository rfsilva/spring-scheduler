package com.taskscheduler;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Task Scheduler API",
        version = "1.0.0",
        description = "API for scheduling and managing automated tasks",
        contact = @Contact(
            name = "Task Scheduler Team",
            email = "support@taskscheduler.com",
            url = "https://taskscheduler.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080/api", description = "Local Development Server"),
        @Server(url = "https://api.taskscheduler.com/api", description = "Production Server")
    }
)
public class TaskSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskSchedulerApplication.class, args);
    }
}