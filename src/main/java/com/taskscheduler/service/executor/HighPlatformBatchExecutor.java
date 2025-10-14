package com.taskscheduler.service.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskscheduler.enums.EndpointType;
import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.model.HighPlatformBatchConfig;
import com.taskscheduler.model.ScheduledTask;
import com.taskscheduler.model.TaskExecution;
import com.taskscheduler.repository.ScheduledTaskRepository;
import com.taskscheduler.service.ScheduledTaskService;
import com.taskscheduler.service.TaskExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@DisallowConcurrentExecution
@Slf4j
public class HighPlatformBatchExecutor implements Job {

    @Autowired
    private ScheduledTaskRepository scheduledTaskRepository;

    @Autowired
    private ScheduledTaskService scheduledTaskService;
    
    @Autowired
    private TaskExecutionService taskExecutionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Long taskId = jobDataMap.getLong("taskId");
        
        log.info("Executing High Platform Batch task with ID: {}", taskId);
        
        // Start execution record
        TaskExecution execution = taskExecutionService.startExecution(taskId);
        
        try {
            ScheduledTask task = scheduledTaskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
            
            HighPlatformBatchConfig config = task.getHighPlatformBatchConfig();
            if (config == null) {
                throw new RuntimeException("High Platform Batch configuration not found for task ID: " + taskId);
            }
            
            String response = executeHighPlatformBatch(config);
            
            // Update task execution status
            scheduledTaskService.updateTaskExecutionStatus(taskId, TaskStatus.COMPLETED, 
                    "High Platform Batch executed successfully");
            
            // Complete execution record with success
            taskExecutionService.completeExecution(
                execution.getId(), 
                TaskStatus.COMPLETED, 
                "High Platform Batch executed successfully", 
                "Response: " + (response.length() > 2000 ? response.substring(0, 2000) + "..." : response)
            );
            
            log.info("High Platform Batch task executed successfully. Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error executing High Platform Batch task with ID: {}", taskId, e);
            
            // Update task execution status
            scheduledTaskService.updateTaskExecutionStatus(taskId, TaskStatus.FAILED, "Error: " + e.getMessage());
            
            // Complete execution record with failure
            taskExecutionService.completeExecution(
                execution.getId(), 
                TaskStatus.FAILED, 
                "Error: " + e.getMessage(), 
                e.toString() + "\n" + 
                java.util.Arrays.stream(e.getStackTrace())
                    .limit(20)
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n"))
            );
        }
    }

    private String executeHighPlatformBatch(HighPlatformBatchConfig config) throws Exception {
        switch (config.getEndpointType()) {
            case CICS:
                return executeCicsTransaction(config);
            case JES:
                return executeJesJob(config);
            case MQ:
                return executeMqTransaction(config);
            case API:
                return executeApiCall(config);
            default:
                throw new IllegalArgumentException("Unsupported endpoint type: " + config.getEndpointType());
        }
    }

    private String executeCicsTransaction(HighPlatformBatchConfig config) throws Exception {
        log.info("Executing CICS transaction: {}", config.getTransactionId());
        
        // In a real implementation, you would use a CICS client library
        // This is a simplified implementation that simulates a CICS call via HTTP
        
        return executeSimulatedMainframeCall(config, "CICS");
    }

    private String executeJesJob(HighPlatformBatchConfig config) throws Exception {
        log.info("Executing JES job: {}", config.getTransactionId());
        
        // In a real implementation, you would use a JES client library
        // This is a simplified implementation that simulates a JES call via HTTP
        
        return executeSimulatedMainframeCall(config, "JES");
    }

    private String executeMqTransaction(HighPlatformBatchConfig config) throws Exception {
        log.info("Executing MQ transaction: {}", config.getTransactionId());
        log.info("Channel: {}, Queue: {}", config.getChannel(), config.getQueue());
        
        // In a real implementation, you would use an MQ client library
        // This is a simplified implementation that simulates an MQ call
        
        // Validate required fields
        if (config.getChannel() == null || config.getQueue() == null) {
            throw new IllegalArgumentException("Channel and Queue are required for MQ endpoint type");
        }
        
        // Simulate MQ call
        return "MQ message sent successfully to queue " + config.getQueue() + " on channel " + config.getChannel();
    }

    private String executeApiCall(HighPlatformBatchConfig config) throws Exception {
        log.info("Executing API call: {}", config.getTransactionId());
        log.info("Host: {}, Port: {}", config.getHost(), config.getPort());
        
        // Validate required fields
        if (config.getHost() == null || config.getPort() == null || config.getSslCertPath() == null) {
            throw new IllegalArgumentException("Host, Port, and SSL Certificate Path are required for API endpoint type");
        }
        
        // Configure timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .setResponseTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .build();
        
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            
            // Create API request
            String url = "https://" + config.getHost() + ":" + config.getPort() + "/" + config.getTransactionId();
            HttpPost request = new HttpPost(url);
            
            // Add credentials
            Map<String, String> credentials = objectMapper.readValue(config.getCredentials(), Map.class);
            if (credentials.containsKey("username") && credentials.containsKey("password")) {
                String auth = credentials.get("username") + ":" + credentials.get("password");
                String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
                request.addHeader("Authorization", "Basic " + encodedAuth);
            }
            
            // Set request body
            StringEntity entity = new StringEntity(config.getPayload(), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            
            // Execute request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String responseBody = reader.lines().collect(Collectors.joining("\n"));
                
                int statusCode = response.getCode();
                if (statusCode >= 200 && statusCode < 300) {
                    return responseBody;
                } else {
                    throw new RuntimeException("API call failed with status code: " + statusCode + ", response: " + responseBody);
                }
            }
        }
    }

    private String executeSimulatedMainframeCall(HighPlatformBatchConfig config, String systemType) throws Exception {
        // Configure timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .setResponseTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .build();
        
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            
            // In a real implementation, you would use a specific client library for the mainframe system
            // For this example, we'll simulate it with an HTTP call to a hypothetical mainframe gateway
            
            // Create simulated mainframe request
            String url = "https://mainframe-gateway.example.com/" + systemType.toLowerCase();
            HttpPost request = new HttpPost(url);
            
            // Add credentials
            Map<String, String> credentials = objectMapper.readValue(config.getCredentials(), Map.class);
            request.addHeader("X-Mainframe-User", credentials.getOrDefault("username", ""));
            request.addHeader("X-Mainframe-Password", credentials.getOrDefault("password", ""));
            
            // Add transaction ID
            request.addHeader("X-Transaction-ID", config.getTransactionId());
            
            // Set payload
            StringEntity entity = new StringEntity(config.getPayload(), ContentType.TEXT_PLAIN);
            request.setEntity(entity);
            
            // For simulation purposes, we'll just return a success message
            return systemType + " transaction " + config.getTransactionId() + " executed successfully";
        }
    }
}