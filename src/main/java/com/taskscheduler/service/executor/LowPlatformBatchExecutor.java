package com.taskscheduler.service.executor;

import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.model.LowPlatformBatchConfig;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@DisallowConcurrentExecution
@Slf4j
public class LowPlatformBatchExecutor implements Job {

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
        
        log.info("Executing Low Platform Batch task with ID: {}", taskId);
        
        // Start execution record
        TaskExecution execution = taskExecutionService.startExecution(taskId);
        
        try {
            ScheduledTask task = scheduledTaskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
            
            LowPlatformBatchConfig config = task.getLowPlatformBatchConfig();
            if (config == null) {
                throw new RuntimeException("Low Platform Batch configuration not found for task ID: " + taskId);
            }
            
            String output = executeBatchCommand(config);
            
            // Update task execution status
            scheduledTaskService.updateTaskExecutionStatus(taskId, TaskStatus.COMPLETED, 
                    "Batch command executed successfully");
            
            // Complete execution record with success
            taskExecutionService.completeExecution(
                execution.getId(), 
                TaskStatus.COMPLETED, 
                "Batch command executed successfully", 
                "Output: " + (output.length() > 2000 ? output.substring(0, 2000) + "..." : output)
            );
            
            // Execute onSuccess command if specified
            if (config.getOnSuccess() != null && !config.getOnSuccess().isEmpty()) {
                executeOnSuccessCommand(config);
            }
            
            log.info("Low Platform Batch task executed successfully. Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error executing Low Platform Batch task with ID: {}", taskId, e);
            
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
            
            // Execute onFailure command if specified
            try {
                ScheduledTask task = scheduledTaskRepository.findById(taskId).orElse(null);
                if (task != null && task.getLowPlatformBatchConfig() != null && 
                        task.getLowPlatformBatchConfig().getOnFailure() != null && 
                        !task.getLowPlatformBatchConfig().getOnFailure().isEmpty()) {
                    executeOnFailureCommand(task.getLowPlatformBatchConfig());
                }
            } catch (Exception ex) {
                log.error("Error executing onFailure command for task ID: {}", taskId, ex);
            }
        }
    }

    private String executeBatchCommand(LowPlatformBatchConfig config) throws Exception {
        List<String> commandParts = new ArrayList<>();
        
        // Add command
        commandParts.add(config.getCommand());
        
        // Add parameters if present
        if (config.getParameters() != null && !config.getParameters().isEmpty()) {
            String[] params = config.getParameters().split("\\s+");
            for (String param : params) {
                commandParts.add(param);
            }
        }
        
        // Create process builder
        ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
        processBuilder.directory(new File(config.getWorkingDirectory()));
        
        // Set run as user if specified
        if (config.getRunAsUser() != null && !config.getRunAsUser().isEmpty()) {
            // In a real implementation, you would use sudo or similar to run as a different user
            log.info("Running as user: {}", config.getRunAsUser());
        }
        
        // Start process
        Process process = processBuilder.start();
        
        // Wait for process to complete with timeout
        boolean completed = process.waitFor(config.getTimeout(), TimeUnit.SECONDS);
        if (!completed) {
            process.destroyForcibly();
            throw new RuntimeException("Batch command timed out after " + config.getTimeout() + " seconds");
        }
        
        // Check exit code
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            // Read error output
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorOutput = errorReader.lines().collect(Collectors.joining("\n"));
            throw new RuntimeException("Batch command failed with exit code: " + exitCode + ", error: " + errorOutput);
        }
        
        // Read output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    private void executeOnSuccessCommand(LowPlatformBatchConfig config) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(config.getOnSuccess().split("\\s+"));
            processBuilder.directory(new File(config.getWorkingDirectory()));
            Process process = processBuilder.start();
            process.waitFor(config.getTimeout(), TimeUnit.SECONDS);
            log.info("OnSuccess command executed for job: {}", config.getJobName());
        } catch (Exception e) {
            log.error("Error executing onSuccess command for job: {}", config.getJobName(), e);
        }
    }

    private void executeOnFailureCommand(LowPlatformBatchConfig config) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(config.getOnFailure().split("\\s+"));
            processBuilder.directory(new File(config.getWorkingDirectory()));
            Process process = processBuilder.start();
            process.waitFor(config.getTimeout(), TimeUnit.SECONDS);
            log.info("OnFailure command executed for job: {}", config.getJobName());
        } catch (Exception e) {
            log.error("Error executing onFailure command for job: {}", config.getJobName(), e);
        }
    }
}