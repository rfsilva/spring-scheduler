package com.taskscheduler.service;

import com.taskscheduler.dto.*;
import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.enums.TaskType;
import com.taskscheduler.exception.TaskNotFoundException;
import com.taskscheduler.model.*;
import com.taskscheduler.repository.ScheduledTaskRepository;
import com.taskscheduler.util.CronExpressionValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskService {

    private final ScheduledTaskRepository scheduledTaskRepository;
    private final Scheduler scheduler;
    private final QuartzJobService quartzJobService;

    public List<ScheduledTaskDTO> getAllTasks() {
        return scheduledTaskRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ScheduledTaskDTO getTaskById(Long id) {
        ScheduledTask task = scheduledTaskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return convertToDTO(task);
    }

    @Transactional
    public ScheduledTaskDTO createTask(ScheduledTaskDTO taskDTO) {
        validateTaskDTO(taskDTO);
        
        ScheduledTask task = convertToEntity(taskDTO);
        task.setExecutionCount(0);
        task.setStatus(TaskStatus.INACTIVE); // Start as inactive
        
        ScheduledTask savedTask = scheduledTaskRepository.save(task);
        
        // Create task configuration based on task type
        createTaskConfiguration(savedTask, taskDTO);
        
        return convertToDTO(savedTask);
    }

    @Transactional
    public ScheduledTaskDTO updateTask(Long id, ScheduledTaskDTO taskDTO) {
        validateTaskDTO(taskDTO);
        
        ScheduledTask existingTask = scheduledTaskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        // If task is active, deactivate it first
        if (existingTask.getStatus() == TaskStatus.ACTIVE) {
            deactivateTask(id);
        }
        
        // Update basic task properties
        existingTask.setName(taskDTO.getName());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setCronExpression(taskDTO.getCronExpression());
        existingTask.setMaxRetries(taskDTO.getMaxRetries());
        existingTask.setRetryDelaySeconds(taskDTO.getRetryDelaySeconds());
        
        // Update task configuration based on task type
        updateTaskConfiguration(existingTask, taskDTO);
        
        ScheduledTask updatedTask = scheduledTaskRepository.save(existingTask);
        
        // If task was active, reactivate it with new configuration
        if (taskDTO.getStatus() == TaskStatus.ACTIVE) {
            activateTask(id);
        }
        
        return convertToDTO(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        ScheduledTask task = scheduledTaskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        // If task is active, deactivate it first
        if (task.getStatus() == TaskStatus.ACTIVE) {
            deactivateTask(id);
        }
        
        scheduledTaskRepository.delete(task);
    }

    @Transactional
    public ScheduledTaskDTO activateTask(Long id) {
        ScheduledTask task = scheduledTaskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        if (task.getStatus() == TaskStatus.ACTIVE) {
            return convertToDTO(task);
        }
        
        // Schedule the task with Quartz
        try {
            quartzJobService.scheduleJob(task);
            task.setStatus(TaskStatus.ACTIVE);
            task.setNextExecution(quartzJobService.calculateNextExecutionTime(task.getCronExpression()));
            return convertToDTO(scheduledTaskRepository.save(task));
        } catch (Exception e) {
            log.error("Failed to activate task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to activate task: " + e.getMessage());
        }
    }

    @Transactional
    public ScheduledTaskDTO deactivateTask(Long id) {
        ScheduledTask task = scheduledTaskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        if (task.getStatus() != TaskStatus.ACTIVE) {
            return convertToDTO(task);
        }
        
        // Unschedule the task from Quartz
        try {
            quartzJobService.unscheduleJob(task);
            task.setStatus(TaskStatus.INACTIVE);
            task.setNextExecution(null);
            return convertToDTO(scheduledTaskRepository.save(task));
        } catch (Exception e) {
            log.error("Failed to deactivate task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to deactivate task: " + e.getMessage());
        }
    }

    @Transactional
    public void updateTaskExecutionStatus(Long taskId, TaskStatus status, String executionMessage) {
        ScheduledTask task = scheduledTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        task.setLastExecution(LocalDateTime.now());
        task.setLastExecutionStatus(status.name());
        task.setLastExecutionMessage(executionMessage);
        task.setExecutionCount(task.getExecutionCount() + 1);
        
        if (status == TaskStatus.COMPLETED || status == TaskStatus.FAILED) {
            task.setNextExecution(quartzJobService.calculateNextExecutionTime(task.getCronExpression()));
        }
        
        scheduledTaskRepository.save(task);
    }

    private void validateTaskDTO(ScheduledTaskDTO taskDTO) {
        // Validate cron expression
        if (!CronExpressionValidator.isValidCronExpression(taskDTO.getCronExpression())) {
            throw new IllegalArgumentException("Invalid cron expression: " + taskDTO.getCronExpression());
        }
        
        // Validate task configuration based on task type
        TaskType taskType = taskDTO.getTaskType();
        
        switch (taskType) {
            case REST_CALL:
                if (taskDTO.getRestTaskConfig() == null) {
                    throw new IllegalArgumentException("REST task configuration is required");
                }
                validateRestTaskConfig(taskDTO.getRestTaskConfig());
                break;
            case SOAP_CALL:
                if (taskDTO.getSoapTaskConfig() == null) {
                    throw new IllegalArgumentException("SOAP task configuration is required");
                }
                break;
            case LOW_PLATFORM_BATCH:
                if (taskDTO.getLowPlatformBatchConfig() == null) {
                    throw new IllegalArgumentException("Low platform batch configuration is required");
                }
                break;
            case HIGH_PLATFORM_BATCH:
                if (taskDTO.getHighPlatformBatchConfig() == null) {
                    throw new IllegalArgumentException("High platform batch configuration is required");
                }
                validateHighPlatformBatchConfig(taskDTO.getHighPlatformBatchConfig());
                break;
            case MESSAGING:
                if (taskDTO.getMessagingTaskConfig() == null) {
                    throw new IllegalArgumentException("Messaging task configuration is required");
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported task type: " + taskType);
        }
    }

    private void validateRestTaskConfig(RestTaskConfigDTO config) {
        // Validate token endpoint, clientId, and clientSecret based on rules
        if (config.getTokenEndpoint() == null) {
            if (config.getClientId() == null || config.getClientSecret() == null) {
                throw new IllegalArgumentException("Client ID and Client Secret are required when Token Endpoint is not provided");
            }
        }
    }

    private void validateHighPlatformBatchConfig(HighPlatformBatchConfigDTO config) {
        // Validate based on endpoint type
        switch (config.getEndpointType()) {
            case MQ:
                if (config.getChannel() == null || config.getQueue() == null) {
                    throw new IllegalArgumentException("Channel and Queue are required for MQ endpoint type");
                }
                break;
            case API:
                if (config.getHost() == null || config.getPort() == null || config.getSslCertPath() == null) {
                    throw new IllegalArgumentException("Host, Port, and SSL Certificate Path are required for API endpoint type");
                }
                break;
            default:
                // No specific validation for other endpoint types
                break;
        }
    }

    private ScheduledTask convertToEntity(ScheduledTaskDTO dto) {
        ScheduledTask task = new ScheduledTask();
        // Não usar o método id() no builder, pois não existe
        if (dto.getId() != null) {
            task.setId(dto.getId());
        }
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setTaskType(dto.getTaskType());
        task.setStatus(dto.getStatus());
        task.setCronExpression(dto.getCronExpression());
        task.setMaxRetries(dto.getMaxRetries());
        task.setRetryDelaySeconds(dto.getRetryDelaySeconds());
        task.setExecutionCount(0);
        return task;
    }

    private ScheduledTaskDTO convertToDTO(ScheduledTask entity) {
        ScheduledTaskDTO dto = ScheduledTaskDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .taskType(entity.getTaskType())
                .status(entity.getStatus())
                .cronExpression(entity.getCronExpression())
                .maxRetries(entity.getMaxRetries())
                .retryDelaySeconds(entity.getRetryDelaySeconds())
                .build();
        
        // Add specific task configuration based on task type
        switch (entity.getTaskType()) {
            case REST_CALL:
                if (entity.getRestTaskConfig() != null) {
                    dto.setRestTaskConfig(convertRestTaskConfigToDTO(entity.getRestTaskConfig()));
                }
                break;
            case SOAP_CALL:
                if (entity.getSoapTaskConfig() != null) {
                    dto.setSoapTaskConfig(convertSoapTaskConfigToDTO(entity.getSoapTaskConfig()));
                }
                break;
            case LOW_PLATFORM_BATCH:
                if (entity.getLowPlatformBatchConfig() != null) {
                    dto.setLowPlatformBatchConfig(convertLowPlatformBatchConfigToDTO(entity.getLowPlatformBatchConfig()));
                }
                break;
            case HIGH_PLATFORM_BATCH:
                if (entity.getHighPlatformBatchConfig() != null) {
                    dto.setHighPlatformBatchConfig(convertHighPlatformBatchConfigToDTO(entity.getHighPlatformBatchConfig()));
                }
                break;
            case MESSAGING:
                if (entity.getMessagingTaskConfig() != null) {
                    dto.setMessagingTaskConfig(convertMessagingTaskConfigToDTO(entity.getMessagingTaskConfig()));
                }
                break;
        }
        
        return dto;
    }

    private RestTaskConfigDTO convertRestTaskConfigToDTO(RestTaskConfig config) {
        return RestTaskConfigDTO.builder()
                .url(config.getUrl())
                .method(config.getMethod())
                .headers(config.getHeaders())
                .body(config.getBody())
                .timeout(config.getTimeout())
                .authType(config.getAuthType())
                .retryPolicy(config.getRetryPolicy())
                .tokenEndpoint(config.getTokenEndpoint())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .certificatePath(config.getCertificatePath())
                .build();
    }

    private SoapTaskConfigDTO convertSoapTaskConfigToDTO(SoapTaskConfig config) {
        return SoapTaskConfigDTO.builder()
                .wsdlUrl(config.getWsdlUrl())
                .operation(config.getOperation())
                .namespace(config.getNamespace())
                .soapAction(config.getSoapAction())
                .requestXml(config.getRequestXml())
                .authType(config.getAuthType())
                .timeout(config.getTimeout())
                .username(config.getUsername())
                .password(config.getPassword())
                .customHeaders(config.getCustomHeaders())
                .build();
    }

    private LowPlatformBatchConfigDTO convertLowPlatformBatchConfigToDTO(LowPlatformBatchConfig config) {
        return LowPlatformBatchConfigDTO.builder()
                .jobName(config.getJobName())
                .command(config.getCommand())
                .parameters(config.getParameters())
                .workingDirectory(config.getWorkingDirectory())
                .timeout(config.getTimeout())
                .runAsUser(config.getRunAsUser())
                .onSuccess(config.getOnSuccess())
                .onFailure(config.getOnFailure())
                .build();
    }

    private HighPlatformBatchConfigDTO convertHighPlatformBatchConfigToDTO(HighPlatformBatchConfig config) {
        return HighPlatformBatchConfigDTO.builder()
                .endpointType(config.getEndpointType())
                .transactionId(config.getTransactionId())
                .payload(config.getPayload())
                .credentials(config.getCredentials())
                .timeout(config.getTimeout())
                .channel(config.getChannel())
                .queue(config.getQueue())
                .host(config.getHost())
                .port(config.getPort())
                .sslCertPath(config.getSslCertPath())
                .build();
    }

    private MessagingTaskConfigDTO convertMessagingTaskConfigToDTO(MessagingTaskConfig config) {
        return MessagingTaskConfigDTO.builder()
                .brokerType(config.getBrokerType())
                .destinationName(config.getDestinationName())
                .messagePayload(config.getMessagePayload())
                .connectionUrl(config.getConnectionUrl())
                .authType(config.getAuthType())
                .headers(config.getHeaders())
                .deliveryMode(config.getDeliveryMode())
                .retryPolicy(config.getRetryPolicy())
                .build();
    }

    private void createTaskConfiguration(ScheduledTask task, ScheduledTaskDTO dto) {
        switch (task.getTaskType()) {
            case REST_CALL:
                createRestTaskConfig(task, dto.getRestTaskConfig());
                break;
            case SOAP_CALL:
                createSoapTaskConfig(task, dto.getSoapTaskConfig());
                break;
            case LOW_PLATFORM_BATCH:
                createLowPlatformBatchConfig(task, dto.getLowPlatformBatchConfig());
                break;
            case HIGH_PLATFORM_BATCH:
                createHighPlatformBatchConfig(task, dto.getHighPlatformBatchConfig());
                break;
            case MESSAGING:
                createMessagingTaskConfig(task, dto.getMessagingTaskConfig());
                break;
        }
    }

    private void updateTaskConfiguration(ScheduledTask task, ScheduledTaskDTO dto) {
        // If task type has changed, remove old configuration and create new one
        if (task.getTaskType() != dto.getTaskType()) {
            // Remove old configuration
            switch (task.getTaskType()) {
                case REST_CALL:
                    task.setRestTaskConfig(null);
                    break;
                case SOAP_CALL:
                    task.setSoapTaskConfig(null);
                    break;
                case LOW_PLATFORM_BATCH:
                    task.setLowPlatformBatchConfig(null);
                    break;
                case HIGH_PLATFORM_BATCH:
                    task.setHighPlatformBatchConfig(null);
                    break;
                case MESSAGING:
                    task.setMessagingTaskConfig(null);
                    break;
            }
            
            // Update task type
            task.setTaskType(dto.getTaskType());
            
            // Create new configuration
            createTaskConfiguration(task, dto);
        } else {
            // Update existing configuration
            switch (task.getTaskType()) {
                case REST_CALL:
                    updateRestTaskConfig(task.getRestTaskConfig(), dto.getRestTaskConfig());
                    break;
                case SOAP_CALL:
                    updateSoapTaskConfig(task.getSoapTaskConfig(), dto.getSoapTaskConfig());
                    break;
                case LOW_PLATFORM_BATCH:
                    updateLowPlatformBatchConfig(task.getLowPlatformBatchConfig(), dto.getLowPlatformBatchConfig());
                    break;
                case HIGH_PLATFORM_BATCH:
                    updateHighPlatformBatchConfig(task.getHighPlatformBatchConfig(), dto.getHighPlatformBatchConfig());
                    break;
                case MESSAGING:
                    updateMessagingTaskConfig(task.getMessagingTaskConfig(), dto.getMessagingTaskConfig());
                    break;
            }
        }
    }

    private void createRestTaskConfig(ScheduledTask task, RestTaskConfigDTO dto) {
        RestTaskConfig config = RestTaskConfig.builder()
                .scheduledTask(task)
                .url(dto.getUrl())
                .method(dto.getMethod())
                .headers(dto.getHeaders())
                .body(dto.getBody())
                .timeout(dto.getTimeout())
                .authType(dto.getAuthType())
                .retryPolicy(dto.getRetryPolicy())
                .tokenEndpoint(dto.getTokenEndpoint())
                .clientId(dto.getClientId())
                .clientSecret(dto.getClientSecret())
                .certificatePath(dto.getCertificatePath())
                .build();
        
        task.setRestTaskConfig(config);
    }

    private void createSoapTaskConfig(ScheduledTask task, SoapTaskConfigDTO dto) {
        SoapTaskConfig config = SoapTaskConfig.builder()
                .scheduledTask(task)
                .wsdlUrl(dto.getWsdlUrl())
                .operation(dto.getOperation())
                .namespace(dto.getNamespace())
                .soapAction(dto.getSoapAction())
                .requestXml(dto.getRequestXml())
                .authType(dto.getAuthType())
                .timeout(dto.getTimeout())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .customHeaders(dto.getCustomHeaders())
                .build();
        
        task.setSoapTaskConfig(config);
    }

    private void createLowPlatformBatchConfig(ScheduledTask task, LowPlatformBatchConfigDTO dto) {
        LowPlatformBatchConfig config = LowPlatformBatchConfig.builder()
                .scheduledTask(task)
                .jobName(dto.getJobName())
                .command(dto.getCommand())
                .parameters(dto.getParameters())
                .workingDirectory(dto.getWorkingDirectory())
                .timeout(dto.getTimeout())
                .runAsUser(dto.getRunAsUser())
                .onSuccess(dto.getOnSuccess())
                .onFailure(dto.getOnFailure())
                .build();
        
        task.setLowPlatformBatchConfig(config);
    }

    private void createHighPlatformBatchConfig(ScheduledTask task, HighPlatformBatchConfigDTO dto) {
        HighPlatformBatchConfig config = HighPlatformBatchConfig.builder()
                .scheduledTask(task)
                .endpointType(dto.getEndpointType())
                .transactionId(dto.getTransactionId())
                .payload(dto.getPayload())
                .credentials(dto.getCredentials())
                .timeout(dto.getTimeout())
                .channel(dto.getChannel())
                .queue(dto.getQueue())
                .host(dto.getHost())
                .port(dto.getPort())
                .sslCertPath(dto.getSslCertPath())
                .build();
        
        task.setHighPlatformBatchConfig(config);
    }

    private void createMessagingTaskConfig(ScheduledTask task, MessagingTaskConfigDTO dto) {
        MessagingTaskConfig config = MessagingTaskConfig.builder()
                .scheduledTask(task)
                .brokerType(dto.getBrokerType())
                .destinationName(dto.getDestinationName())
                .messagePayload(dto.getMessagePayload())
                .connectionUrl(dto.getConnectionUrl())
                .authType(dto.getAuthType())
                .headers(dto.getHeaders())
                .deliveryMode(dto.getDeliveryMode())
                .retryPolicy(dto.getRetryPolicy())
                .build();
        
        task.setMessagingTaskConfig(config);
    }

    private void updateRestTaskConfig(RestTaskConfig config, RestTaskConfigDTO dto) {
        config.setUrl(dto.getUrl());
        config.setMethod(dto.getMethod());
        config.setHeaders(dto.getHeaders());
        config.setBody(dto.getBody());
        config.setTimeout(dto.getTimeout());
        config.setAuthType(dto.getAuthType());
        config.setRetryPolicy(dto.getRetryPolicy());
        config.setTokenEndpoint(dto.getTokenEndpoint());
        config.setClientId(dto.getClientId());
        config.setClientSecret(dto.getClientSecret());
        config.setCertificatePath(dto.getCertificatePath());
    }

    private void updateSoapTaskConfig(SoapTaskConfig config, SoapTaskConfigDTO dto) {
        config.setWsdlUrl(dto.getWsdlUrl());
        config.setOperation(dto.getOperation());
        config.setNamespace(dto.getNamespace());
        config.setSoapAction(dto.getSoapAction());
        config.setRequestXml(dto.getRequestXml());
        config.setAuthType(dto.getAuthType());
        config.setTimeout(dto.getTimeout());
        config.setUsername(dto.getUsername());
        config.setPassword(dto.getPassword());
        config.setCustomHeaders(dto.getCustomHeaders());
    }

    private void updateLowPlatformBatchConfig(LowPlatformBatchConfig config, LowPlatformBatchConfigDTO dto) {
        config.setJobName(dto.getJobName());
        config.setCommand(dto.getCommand());
        config.setParameters(dto.getParameters());
        config.setWorkingDirectory(dto.getWorkingDirectory());
        config.setTimeout(dto.getTimeout());
        config.setRunAsUser(dto.getRunAsUser());
        config.setOnSuccess(dto.getOnSuccess());
        config.setOnFailure(dto.getOnFailure());
    }

    private void updateHighPlatformBatchConfig(HighPlatformBatchConfig config, HighPlatformBatchConfigDTO dto) {
        config.setEndpointType(dto.getEndpointType());
        config.setTransactionId(dto.getTransactionId());
        config.setPayload(dto.getPayload());
        config.setCredentials(dto.getCredentials());
        config.setTimeout(dto.getTimeout());
        config.setChannel(dto.getChannel());
        config.setQueue(dto.getQueue());
        config.setHost(dto.getHost());
        config.setPort(dto.getPort());
        config.setSslCertPath(dto.getSslCertPath());
    }

    private void updateMessagingTaskConfig(MessagingTaskConfig config, MessagingTaskConfigDTO dto) {
        config.setBrokerType(dto.getBrokerType());
        config.setDestinationName(dto.getDestinationName());
        config.setMessagePayload(dto.getMessagePayload());
        config.setConnectionUrl(dto.getConnectionUrl());
        config.setAuthType(dto.getAuthType());
        config.setHeaders(dto.getHeaders());
        config.setDeliveryMode(dto.getDeliveryMode());
        config.setRetryPolicy(dto.getRetryPolicy());
    }
}