package com.taskscheduler.service.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskscheduler.enums.BrokerType;
import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.model.MessagingTaskConfig;
import com.taskscheduler.model.ScheduledTask;
import com.taskscheduler.model.TaskExecution;
import com.taskscheduler.repository.ScheduledTaskRepository;
import com.taskscheduler.service.ScheduledTaskService;
import com.taskscheduler.service.TaskExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@DisallowConcurrentExecution
@Slf4j
public class MessagingTaskExecutor implements Job {

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
        
        log.info("Executing Messaging task with ID: {}", taskId);
        
        // Start execution record
        TaskExecution execution = taskExecutionService.startExecution(taskId);
        
        try {
            ScheduledTask task = scheduledTaskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
            
            MessagingTaskConfig config = task.getMessagingTaskConfig();
            if (config == null) {
                throw new RuntimeException("Messaging task configuration not found for task ID: " + taskId);
            }
            
            String result = sendMessage(config);
            
            // Update task execution status
            scheduledTaskService.updateTaskExecutionStatus(taskId, TaskStatus.COMPLETED, 
                    "Message sent successfully");
            
            // Complete execution record with success
            taskExecutionService.completeExecution(
                execution.getId(), 
                TaskStatus.COMPLETED, 
                "Message sent successfully", 
                "Result: " + result
            );
            
            log.info("Messaging task executed successfully. Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error executing Messaging task with ID: {}", taskId, e);
            
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

    private String sendMessage(MessagingTaskConfig config) throws Exception {
        switch (config.getBrokerType()) {
            case RABBITMQ:
                return sendRabbitMQMessage(config);
            case ACTIVEMQ:
                return sendActiveMQMessage(config);
            case IBMMQ:
                return sendIBMMQMessage(config);
            case KAFKA:
                return sendKafkaMessage(config);
            case SQS:
                return sendSQSMessage(config);
            default:
                throw new IllegalArgumentException("Unsupported broker type: " + config.getBrokerType());
        }
    }

    private String sendRabbitMQMessage(MessagingTaskConfig config) throws Exception {
        log.info("Sending RabbitMQ message to: {}", config.getDestinationName());
        
        // In a real implementation, you would use the RabbitMQ client
        // This is a simplified implementation that simulates sending a message
        
        Map<String, String> headers = objectMapper.readValue(config.getHeaders(), Map.class);
        log.info("Message headers: {}", headers);
        log.info("Message payload: {}", config.getMessagePayload());
        log.info("Delivery mode: {}", config.getDeliveryMode());
        
        // Simulate sending message
        return "Message sent to RabbitMQ destination: " + config.getDestinationName();
    }

    private String sendActiveMQMessage(MessagingTaskConfig config) throws Exception {
        log.info("Sending ActiveMQ message to: {}", config.getDestinationName());
        
        // In a real implementation, you would use the ActiveMQ client
        // This is a simplified implementation that simulates sending a message
        
        Map<String, String> headers = objectMapper.readValue(config.getHeaders(), Map.class);
        log.info("Message headers: {}", headers);
        log.info("Message payload: {}", config.getMessagePayload());
        log.info("Delivery mode: {}", config.getDeliveryMode());
        
        // Simulate sending message
        return "Message sent to ActiveMQ destination: " + config.getDestinationName();
    }

    private String sendIBMMQMessage(MessagingTaskConfig config) throws Exception {
        log.info("Sending IBM MQ message to: {}", config.getDestinationName());
        
        // In a real implementation, you would use the IBM MQ client
        // This is a simplified implementation that simulates sending a message
        
        Map<String, String> headers = objectMapper.readValue(config.getHeaders(), Map.class);
        log.info("Message headers: {}", headers);
        log.info("Message payload: {}", config.getMessagePayload());
        log.info("Delivery mode: {}", config.getDeliveryMode());
        
        // Simulate sending message
        return "Message sent to IBM MQ destination: " + config.getDestinationName();
    }

    private String sendKafkaMessage(MessagingTaskConfig config) throws Exception {
        log.info("Sending Kafka message to topic: {}", config.getDestinationName());
        
        // In a real implementation, you would use the Kafka client
        // This is a simplified implementation that simulates sending a message
        
        Map<String, String> headers = objectMapper.readValue(config.getHeaders(), Map.class);
        log.info("Message headers: {}", headers);
        log.info("Message payload: {}", config.getMessagePayload());
        
        // Simulate sending message
        return "Message sent to Kafka topic: " + config.getDestinationName();
    }

    private String sendSQSMessage(MessagingTaskConfig config) throws Exception {
        log.info("Sending SQS message to queue: {}", config.getDestinationName());
        
        // In a real implementation, you would use the AWS SDK for SQS
        // This is a simplified implementation that simulates sending a message
        
        Map<String, String> headers = objectMapper.readValue(config.getHeaders(), Map.class);
        log.info("Message headers: {}", headers);
        log.info("Message payload: {}", config.getMessagePayload());
        
        // Simulate sending message
        return "Message sent to SQS queue: " + config.getDestinationName();
    }
}