package com.taskscheduler.dto;

import com.taskscheduler.enums.RestAuthType;
import com.taskscheduler.enums.SoapAuthType;
import com.taskscheduler.enums.HttpMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestTaskConfigDTO {
    
    @NotBlank(message = "URL is required")
    private String url;
    
    @NotNull(message = "HTTP method is required")
    private HttpMethod method;
    
    @NotBlank(message = "Headers are required")
    private String headers;
    
    private String body;
    
    @NotNull(message = "Timeout is required")
    private Integer timeout;
    
    @NotNull(message = "Authentication type is required")
    private RestAuthType authType;
    
    private String retryPolicy;
    
    private String tokenEndpoint;
    
    private String clientId;
    
    private String clientSecret;
    
    private String certificatePath;
}