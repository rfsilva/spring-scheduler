package com.taskscheduler.controller;

import com.taskscheduler.dto.TaskExecutionDTO;
import com.taskscheduler.service.TaskExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/executions")
@RequiredArgsConstructor
public class TaskExecutionController {

    private final TaskExecutionService taskExecutionService;

    @GetMapping
    public ResponseEntity<List<TaskExecutionDTO>> getTaskExecutionHistory(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskExecutionService.getTaskExecutionHistory(taskId));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<TaskExecutionDTO>> getTaskExecutionHistoryPaged(
            @PathVariable Long taskId,
            Pageable pageable) {
        return ResponseEntity.ok(taskExecutionService.getTaskExecutionHistoryPaged(taskId, pageable));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TaskExecutionDTO>> getTaskExecutionHistoryByDateRange(
            @PathVariable Long taskId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(taskExecutionService.getTaskExecutionHistoryByDateRange(taskId, start, end));
    }
}