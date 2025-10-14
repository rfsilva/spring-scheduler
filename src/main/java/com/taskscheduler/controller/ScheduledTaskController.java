package com.taskscheduler.controller;

import com.taskscheduler.dto.ScheduledTaskDTO;
import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.service.ScheduledTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class ScheduledTaskController {

    private final ScheduledTaskService scheduledTaskService;

    @GetMapping
    public ResponseEntity<List<ScheduledTaskDTO>> getAllTasks() {
        return ResponseEntity.ok(scheduledTaskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledTaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduledTaskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<ScheduledTaskDTO> createTask(@Valid @RequestBody ScheduledTaskDTO taskDTO) {
        return new ResponseEntity<>(scheduledTaskService.createTask(taskDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduledTaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody ScheduledTaskDTO taskDTO) {
        return ResponseEntity.ok(scheduledTaskService.updateTask(id, taskDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        scheduledTaskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ScheduledTaskDTO> activateTask(@PathVariable Long id) {
        return ResponseEntity.ok(scheduledTaskService.activateTask(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ScheduledTaskDTO> deactivateTask(@PathVariable Long id) {
        return ResponseEntity.ok(scheduledTaskService.deactivateTask(id));
    }
}