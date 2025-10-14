package com.taskscheduler.model;

import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.enums.TaskType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledTask extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private TaskStatus status;

    @Column(nullable = false)
    private String cronExpression;

    @Column(name = "next_execution")
    private LocalDateTime nextExecution;

    @Column(name = "last_execution")
    private LocalDateTime lastExecution;

    @Column(name = "last_execution_status")
    private String lastExecutionStatus;

    @Column(name = "last_execution_message", columnDefinition = "TEXT")
    private String lastExecutionMessage;

    @Column(name = "execution_count")
    private Integer executionCount;

    @Column(name = "max_retries")
    private Integer maxRetries;

    @Column(name = "retry_delay_seconds")
    private Integer retryDelaySeconds;

    @Column(name = "task_data", columnDefinition = "TEXT")
    private String taskData;

    @OneToOne(mappedBy = "scheduledTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private RestTaskConfig restTaskConfig;

    @OneToOne(mappedBy = "scheduledTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private SoapTaskConfig soapTaskConfig;

    @OneToOne(mappedBy = "scheduledTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private LowPlatformBatchConfig lowPlatformBatchConfig;

    @OneToOne(mappedBy = "scheduledTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private HighPlatformBatchConfig highPlatformBatchConfig;

    @OneToOne(mappedBy = "scheduledTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private MessagingTaskConfig messagingTaskConfig;
}