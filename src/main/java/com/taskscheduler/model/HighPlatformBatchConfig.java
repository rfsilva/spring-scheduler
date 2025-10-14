package com.taskscheduler.model;

import com.taskscheduler.enums.EndpointType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "high_platform_batch_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HighPlatformBatchConfig extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "scheduled_task_id", nullable = false)
    private ScheduledTask scheduledTask;

    @Enumerated(EnumType.STRING)
    @Column(name = "endpoint_type", nullable = false, columnDefinition = "VARCHAR(20)")
    private EndpointType endpointType;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String credentials;

    @Column(nullable = false)
    private Integer timeout;

    private String channel;

    private String queue;

    private String host;

    private String port;

    @Column(name = "ssl_cert_path")
    private String sslCertPath;
}