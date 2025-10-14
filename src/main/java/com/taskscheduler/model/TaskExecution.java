package com.taskscheduler.model;

import com.taskscheduler.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_executions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskExecution extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "scheduled_task_id", nullable = false)
    private ScheduledTask scheduledTask;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(50)")
    private TaskStatus status;

    @Column(name = "execution_message", columnDefinition = "TEXT")
    private String executionMessage;

    @Column(name = "execution_details", columnDefinition = "TEXT")
    private String executionDetails;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "duration_ms")
    private Long durationMs;
}