package com.taskscheduler.service.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.model.RestTaskConfig;
import com.taskscheduler.model.ScheduledTask;
import com.taskscheduler.model.TaskExecution;
import com.taskscheduler.repository.ScheduledTaskRepository;
import com.taskscheduler.service.ScheduledTaskService;
import com.taskscheduler.service.TaskExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.*;
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
public class RestTaskExecutor implements Job {

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
        
        log.info("Executing REST task with ID: {}", taskId);
        
        // Start execution record
        TaskExecution execution = taskExecutionService.startExecution(taskId);
        
        try {
            ScheduledTask task = scheduledTaskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
            
            RestTaskConfig config = task.getRestTaskConfig();
            if (config == null) {
                throw new RuntimeException("REST task configuration not found for task ID: " + taskId);
            }
            
            String response = executeRestCall(config);
            
            // Update task execution status
            scheduledTaskService.updateTaskExecutionStatus(taskId, TaskStatus.COMPLETED, 
                    "REST call executed successfully");
            
            // Complete execution record with success
            taskExecutionService.completeExecution(
                execution.getId(), 
                TaskStatus.COMPLETED, 
                "REST call executed successfully", 
                "Response: " + (response.length() > 2000 ? response.substring(0, 2000) + "..." : response)
            );
            
            log.info("REST task executed successfully. Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error executing REST task with ID: {}", taskId, e);
            
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

    private String executeRestCall(RestTaskConfig config) throws Exception {
        // Configure timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .setResponseTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .build();
        
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            
            // Create request based on method
            HttpUriRequestBase request = createRequest(config);
            
            // Add headers
            Map<String, String> headers = objectMapper.readValue(config.getHeaders(), Map.class);
            headers.forEach(request::addHeader);
            
            // Add authentication
            addAuthentication(request, config);
            
            // Add body if present
            if (config.getBody() != null && !config.getBody().isEmpty() && 
                    !(request instanceof HttpGet || request instanceof HttpDelete)) {
                StringEntity entity = new StringEntity(config.getBody(), ContentType.APPLICATION_JSON);
                
                // Verificar o tipo de requisição e adicionar a entidade de forma apropriada
                if (request instanceof HttpPost) {
                    ((HttpPost) request).setEntity(entity);
                } else if (request instanceof HttpPut) {
                    ((HttpPut) request).setEntity(entity);
                } else if (request instanceof HttpPatch) {
                    ((HttpPatch) request).setEntity(entity);
                }
            }
            
            // Execute request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String responseBody = reader.lines().collect(Collectors.joining("\n"));
                
                int statusCode = response.getCode();
                if (statusCode >= 200 && statusCode < 300) {
                    return responseBody;
                } else {
                    throw new RuntimeException("REST call failed with status code: " + statusCode + ", response: " + responseBody);
                }
            }
        }
    }

    private HttpUriRequestBase createRequest(RestTaskConfig config) {
        switch (config.getMethod()) {
            case GET:
                return new HttpGet(config.getUrl());
            case POST:
                return new HttpPost(config.getUrl());
            case PUT:
                return new HttpPut(config.getUrl());
            case DELETE:
                return new HttpDelete(config.getUrl());
            case PATCH:
                return new HttpPatch(config.getUrl());
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + config.getMethod());
        }
    }

    private void addAuthentication(HttpUriRequestBase request, RestTaskConfig config) {
        switch (config.getAuthType()) {
            case NONE:
                // No authentication needed
                break;
            case BASIC:
                if (config.getClientId() != null && config.getClientSecret() != null) {
                    String auth = config.getClientId() + ":" + config.getClientSecret();
                    String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
                    request.addHeader("Authorization", "Basic " + encodedAuth);
                }
                break;
            case BEARER:
                // For simplicity, we're assuming the token is provided in the clientSecret field
                // In a real implementation, you would use the tokenEndpoint to get a token
                if (config.getClientSecret() != null) {
                    request.addHeader("Authorization", "Bearer " + config.getClientSecret());
                }
                break;
            case MTLS:
                // MTLS authentication would require configuring the HttpClient with client certificates
                // This is a simplified implementation
                log.info("MTLS authentication requires certificate configuration: {}", config.getCertificatePath());
                break;
            default:
                throw new IllegalArgumentException("Unsupported authentication type: " + config.getAuthType());
        }
    }
}