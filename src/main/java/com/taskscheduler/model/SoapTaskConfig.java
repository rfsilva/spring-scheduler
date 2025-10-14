package com.taskscheduler.model;

import com.taskscheduler.enums.SoapAuthType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "soap_task_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoapTaskConfig extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "scheduled_task_id", nullable = false)
    private ScheduledTask scheduledTask;

    @Column(name = "wsdl_url", nullable = false)
    private String wsdlUrl;

    @Column(nullable = false)
    private String operation;

    @Column(nullable = false)
    private String namespace;

    @Column(name = "soap_action")
    private String soapAction;

    @Column(name = "request_xml", nullable = false, columnDefinition = "TEXT")
    private String requestXml;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false, columnDefinition = "VARCHAR(20)")
    private SoapAuthType authType;

    @Column(nullable = false)
    private Integer timeout;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "custom_headers", columnDefinition = "TEXT")
    private String customHeaders;
}