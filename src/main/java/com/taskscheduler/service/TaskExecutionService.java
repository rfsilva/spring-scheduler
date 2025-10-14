package com.taskscheduler.service;

import com.taskscheduler.dto.TaskExecutionDTO;
import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.exception.TaskNotFoundException;
import com.taskscheduler.model.ScheduledTask;
import com.taskscheduler.model.TaskExecution;
import com.taskscheduler.repository.ScheduledTaskRepository;
import com.taskscheduler.repository.TaskExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskExecutionService {

    private final TaskExecutionRepository taskExecutionRepository;
    private final ScheduledTaskRepository scheduledTaskRepository;

    public TaskExecution startExecution(Long taskId) {
        ScheduledTask task = scheduledTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        TaskExecution execution = TaskExecution.builder()
                .scheduledTask(task)
                .startTime(LocalDateTime.now())
                .status(TaskStatus.EXECUTING)
                .retryCount(0)
                .build();
        
        TaskExecution savedExecution = taskExecutionRepository.save(execution);
        log.info("Started execution for task: {} (ID: {}), execution ID: {}", task.getName(), taskId, savedExecution.getId());
        return savedExecution;
    }

    public TaskExecution completeExecution(Long executionId, TaskStatus status, String message, String details) {
        TaskExecution execution = taskExecutionRepository.findById(executionId)
                .orElseThrow(() -> new RuntimeException("Execution not found with id: " + executionId));
        
        LocalDateTime endTime = LocalDateTime.now();
        long durationMs = ChronoUnit.MILLIS.between(execution.getStartTime(), endTime);
        
        execution.setEndTime(endTime);
        execution.setStatus(status);
        execution.setExecutionMessage(message);
        execution.setExecutionDetails(details);
        execution.setDurationMs(durationMs);
        
        TaskExecution savedExecution = taskExecutionRepository.save(execution);
        
        log.info("Completed execution ID: {} for task: {} (ID: {}) with status: {}, duration: {} ms",
                executionId, 
                execution.getScheduledTask().getName(), 
                execution.getScheduledTask().getId(),
                status,
                durationMs);
        
        if (status == TaskStatus.FAILED) {
            log.error("Execution failed: {}", message);
            if (details != null && !details.isEmpty()) {
                log.debug("Execution details: {}", details);
            }
        }
        
        return savedExecution;
    }

    public TaskExecution updateRetryCount(Long executionId, Integer retryCount) {
        TaskExecution execution = taskExecutionRepository.findById(executionId)
                .orElseThrow(() -> new RuntimeException("Execution not found with id: " + executionId));
        
        execution.setRetryCount(retryCount);
        return taskExecutionRepository.save(execution);
    }

    public List<TaskExecutionDTO> getTaskExecutionHistory(Long taskId) {
        // Verify task exists
        if (!scheduledTaskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
        
        return taskExecutionRepository.findByScheduledTaskId(taskId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<TaskExecutionDTO> getTaskExecutionHistoryPaged(Long taskId, Pageable pageable) {
        // Verify task exists
        if (!scheduledTaskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
        
        return taskExecutionRepository.findByScheduledTaskId(taskId, pageable)
                .map(this::convertToDTO);
    }

    public List<TaskExecutionDTO> getTaskExecutionHistoryByDateRange(Long taskId, LocalDateTime start, LocalDateTime end) {
        // Verify task exists
        if (!scheduledTaskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
        
        return taskExecutionRepository.findByScheduledTaskIdAndStartTimeBetween(taskId, start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TaskExecutionDTO convertToDTO(TaskExecution execution) {
        return TaskExecutionDTO.builder()
                .id(execution.getId())
                .taskId(execution.getScheduledTask().getId())
                .taskName(execution.getScheduledTask().getName())
                .startTime(execution.getStartTime())
                .endTime(execution.getEndTime())
                .status(execution.getStatus())
                .executionMessage(execution.getExecutionMessage())
                .executionDetails(execution.getExecutionDetails())
                .retryCount(execution.getRetryCount())
                .durationMs(execution.getDurationMs())
                .build();
    }
}