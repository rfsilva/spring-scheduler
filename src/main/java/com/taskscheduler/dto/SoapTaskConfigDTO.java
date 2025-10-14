package com.taskscheduler.dto;

import com.taskscheduler.enums.SoapAuthType;
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
public class SoapTaskConfigDTO {
    
    @NotBlank(message = "WSDL URL is required")
    private String wsdlUrl;
    
    @NotBlank(message = "Operation is required")
    private String operation;
    
    @NotBlank(message = "Namespace is required")
    private String namespace;
    
    private String soapAction;
    
    @NotBlank(message = "Request XML is required")
    private String requestXml;
    
    @NotNull(message = "Authentication type is required")
    private SoapAuthType authType;
    
    @NotNull(message = "Timeout is required")
    private Integer timeout;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private String customHeaders;
}