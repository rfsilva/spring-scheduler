package com.taskscheduler.dto;

import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.enums.TaskType;
import jakarta.validation.Valid;
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
public class ScheduledTaskDTO {
    
    private Long id;
    
    @NotBlank(message = "Task name is required")
    private String name;
    
    @NotBlank(message = "Task description is required")
    private String description;
    
    @NotNull(message = "Task type is required")
    private TaskType taskType;
    
    @NotNull(message = "Task status is required")
    private TaskStatus status;
    
    @NotBlank(message = "Cron expression is required")
    private String cronExpression;
    
    private Integer maxRetries;
    
    private Integer retryDelaySeconds;
    
    @Valid
    private RestTaskConfigDTO restTaskConfig;
    
    @Valid
    private SoapTaskConfigDTO soapTaskConfig;
    
    @Valid
    private LowPlatformBatchConfigDTO lowPlatformBatchConfig;
    
    @Valid
    private HighPlatformBatchConfigDTO highPlatformBatchConfig;
    
    @Valid
    private MessagingTaskConfigDTO messagingTaskConfig;
}