package com.taskscheduler.repository;

import com.taskscheduler.model.TaskExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long> {
    
    List<TaskExecution> findByScheduledTaskId(Long taskId);
    
    Page<TaskExecution> findByScheduledTaskId(Long taskId, Pageable pageable);
    
    List<TaskExecution> findByScheduledTaskIdAndStartTimeBetween(Long taskId, LocalDateTime start, LocalDateTime end);
    
    List<TaskExecution> findByStatusAndStartTimeBefore(String status, LocalDateTime time);
}