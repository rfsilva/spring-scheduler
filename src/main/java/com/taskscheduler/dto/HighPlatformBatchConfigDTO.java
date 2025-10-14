package com.taskscheduler.dto;

import com.taskscheduler.enums.EndpointType;
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
public class HighPlatformBatchConfigDTO {
    
    @NotNull(message = "Endpoint type is required")
    private EndpointType endpointType;
    
    @NotBlank(message = "Transaction ID is required")
    private String transactionId;
    
    @NotBlank(message = "Payload is required")
    private String payload;
    
    @NotBlank(message = "Credentials are required")
    private String credentials;
    
    @NotNull(message = "Timeout is required")
    private Integer timeout;
    
    private String channel;
    
    private String queue;
    
    private String host;
    
    private String port;
    
    private String sslCertPath;
}