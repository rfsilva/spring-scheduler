package com.taskscheduler.model;

import com.taskscheduler.enums.RestAuthType;
import com.taskscheduler.enums.SoapAuthType;
import com.taskscheduler.enums.HttpMethod;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rest_task_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestTaskConfig extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "scheduled_task_id", nullable = false)
    private ScheduledTask scheduledTask;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private HttpMethod method;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String headers;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    private Integer timeout;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false, columnDefinition = "VARCHAR(20)")
    private RestAuthType authType;

    @Column(name = "retry_policy", columnDefinition = "TEXT")
    private String retryPolicy;

    @Column(name = "token_endpoint")
    private String tokenEndpoint;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "certificate_path")
    private String certificatePath;
}