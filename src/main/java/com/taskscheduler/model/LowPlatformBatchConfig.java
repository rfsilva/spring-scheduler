package com.taskscheduler.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "low_platform_batch_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LowPlatformBatchConfig extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "scheduled_task_id", nullable = false)
    private ScheduledTask scheduledTask;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Column(nullable = false)
    private String command;

    private String parameters;

    @Column(name = "working_directory", nullable = false)
    private String workingDirectory;

    @Column(nullable = false)
    private Integer timeout;

    @Column(name = "run_as_user")
    private String runAsUser;

    @Column(name = "on_success")
    private String onSuccess;

    @Column(name = "on_failure")
    private String onFailure;
}