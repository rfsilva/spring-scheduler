package com.taskscheduler.model;

import com.taskscheduler.enums.MessagingAuthType;
import com.taskscheduler.enums.SoapAuthType;
import com.taskscheduler.enums.BrokerType;
import com.taskscheduler.enums.DeliveryMode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messaging_task_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagingTaskConfig extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "scheduled_task_id", nullable = false)
    private ScheduledTask scheduledTask;

    @Enumerated(EnumType.STRING)
    @Column(name = "broker_type", nullable = false, columnDefinition = "VARCHAR(20)")
    private BrokerType brokerType;

    @Column(name = "destination_name", nullable = false)
    private String destinationName;

    @Column(name = "message_payload", nullable = false, columnDefinition = "TEXT")
    private String messagePayload;

    @Column(name = "connection_url", nullable = false)
    private String connectionUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false, columnDefinition = "VARCHAR(20)")
    private MessagingAuthType authType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String headers;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", nullable = false, columnDefinition = "VARCHAR(20)")
    private DeliveryMode deliveryMode;

    @Column(name = "retry_policy", columnDefinition = "TEXT")
    private String retryPolicy;
}