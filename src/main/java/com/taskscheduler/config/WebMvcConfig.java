package com.taskscheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configuração para garantir que os recursos do Swagger sejam acessíveis
        registry.addResourceHandler(contextPath + "/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirecionamento para a página do Swagger UI
        registry.addViewController(contextPath + "/swagger-ui/")
                .setViewName("forward:" + contextPath + "/swagger-ui/index.html");
        
        // Redirecionamento da raiz para o Swagger UI
        registry.addViewController(contextPath + "/")
                .setViewName("redirect:" + contextPath + "/swagger-ui.html");
    }
}