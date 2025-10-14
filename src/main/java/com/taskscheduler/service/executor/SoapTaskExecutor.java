package com.taskscheduler.service.executor;

import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.model.ScheduledTask;
import com.taskscheduler.model.SoapTaskConfig;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@DisallowConcurrentExecution
@Slf4j
public class SoapTaskExecutor implements Job {

    @Autowired
    private ScheduledTaskRepository scheduledTaskRepository;

    @Autowired
    private ScheduledTaskService scheduledTaskService;
    
    @Autowired
    private TaskExecutionService taskExecutionService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Long taskId = jobDataMap.getLong("taskId");
        
        log.info("Executing SOAP task with ID: {}", taskId);
        
        // Start execution record
        TaskExecution execution = taskExecutionService.startExecution(taskId);
        
        try {
            ScheduledTask task = scheduledTaskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
            
            SoapTaskConfig config = task.getSoapTaskConfig();
            if (config == null) {
                throw new RuntimeException("SOAP task configuration not found for task ID: " + taskId);
            }
            
            String response = executeSoapCall(config);
            
            // Update task execution status
            scheduledTaskService.updateTaskExecutionStatus(taskId, TaskStatus.COMPLETED, 
                    "SOAP call executed successfully");
            
            // Complete execution record with success
            taskExecutionService.completeExecution(
                execution.getId(), 
                TaskStatus.COMPLETED, 
                "SOAP call executed successfully", 
                "Response: " + (response.length() > 2000 ? response.substring(0, 2000) + "..." : response)
            );
            
            log.info("SOAP task executed successfully. Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error executing SOAP task with ID: {}", taskId, e);
            
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

    private String executeSoapCall(SoapTaskConfig config) throws Exception {
        // Configure timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .setResponseTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .build();
        
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            
            // Create SOAP request
            HttpPost request = new HttpPost(config.getWsdlUrl());
            
            // Set SOAP headers
            request.addHeader("Content-Type", "text/xml;charset=UTF-8");
            
            if (config.getSoapAction() != null && !config.getSoapAction().isEmpty()) {
                request.addHeader("SOAPAction", config.getSoapAction());
            }
            
            // Add authentication
            addAuthentication(request, config);
            
            // Add custom headers if present
            if (config.getCustomHeaders() != null && !config.getCustomHeaders().isEmpty()) {
                // In a real implementation, parse and add custom headers
                log.info("Adding custom headers: {}", config.getCustomHeaders());
            }
            
            // Set SOAP request body
            StringEntity entity = new StringEntity(config.getRequestXml(), ContentType.TEXT_XML);
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
                    throw new RuntimeException("SOAP call failed with status code: " + statusCode + ", response: " + responseBody);
                }
            }
        }
    }

    private void addAuthentication(HttpPost request, SoapTaskConfig config) {
        switch (config.getAuthType()) {
            case NONE:
                // No authentication needed
                break;
            case BASIC:
                String auth = config.getUsername() + ":" + config.getPassword();
                String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
                request.addHeader("Authorization", "Basic " + encodedAuth);
                break;
            case WSSECURITY:
                // In a real implementation, you would add WS-Security headers to the SOAP envelope
                log.info("WS-Security authentication requires modifying the SOAP envelope");
                break;
            case MTLS:
                // MTLS authentication would require configuring the HttpClient with client certificates
                log.info("MTLS authentication requires certificate configuration");
                break;
            default:
                throw new IllegalArgumentException("Unsupported authentication type: " + config.getAuthType());
        }
    }
}