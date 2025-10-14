package com.taskscheduler.dto;

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
public class LowPlatformBatchConfigDTO {
    
    @NotBlank(message = "Job name is required")
    private String jobName;
    
    @NotBlank(message = "Command is required")
    private String command;
    
    private String parameters;
    
    @NotBlank(message = "Working directory is required")
    private String workingDirectory;
    
    @NotNull(message = "Timeout is required")
    private Integer timeout;
    
    private String runAsUser;
    
    private String onSuccess;
    
    private String onFailure;
}