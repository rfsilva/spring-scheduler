package com.taskscheduler.dto;

import com.taskscheduler.enums.MessagingAuthType;
import com.taskscheduler.enums.SoapAuthType;
import com.taskscheduler.enums.BrokerType;
import com.taskscheduler.enums.DeliveryMode;
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
public class MessagingTaskConfigDTO {
    
    @NotNull(message = "Broker type is required")
    private BrokerType brokerType;
    
    @NotBlank(message = "Destination name is required")
    private String destinationName;
    
    @NotBlank(message = "Message payload is required")
    private String messagePayload;
    
    @NotBlank(message = "Connection URL is required")
    private String connectionUrl;
    
    @NotNull(message = "Authentication type is required")
    private MessagingAuthType authType;
    
    @NotBlank(message = "Headers are required")
    private String headers;
    
    @NotNull(message = "Delivery mode is required")
    private DeliveryMode deliveryMode;
    
    private String retryPolicy;
}