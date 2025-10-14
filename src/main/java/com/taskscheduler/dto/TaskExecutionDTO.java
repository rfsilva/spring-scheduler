package com.taskscheduler.dto;

import com.taskscheduler.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecutionDTO {
    
    private Long id;
    private Long taskId;
    private String taskName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TaskStatus status;
    private String executionMessage;
    private String executionDetails;
    private Integer retryCount;
    private Long durationMs;
}