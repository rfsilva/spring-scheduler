package com.example.api.repository;

import com.example.api.model.Task;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TaskRepository {
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public TaskRepository() {
        // Add some initial tasks
        Task task1 = new Task(idCounter.getAndIncrement(), "Complete project documentation", 
                "Write comprehensive documentation for the API", "PENDING", 
                LocalDateTime.now().minusDays(5), LocalDateTime.now(), 2);
        
        Task task2 = new Task(idCounter.getAndIncrement(), "Fix security vulnerabilities", 
                "Address the security issues identified in the last audit", "IN_PROGRESS", 
                LocalDateTime.now().minusDays(3), LocalDateTime.now(), 1);
        
        Task task3 = new Task(idCounter.getAndIncrement(), "Implement new features", 
                "Add the features requested by the client", "PENDING", 
                LocalDateTime.now().minusDays(1), LocalDateTime.now(), 3);
        
        tasks.put(task1.getId(), task1);
        tasks.put(task2.getId(), task2);
        tasks.put(task3.getId(), task3);
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Task findById(Long id) {
        return tasks.get(id);
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idCounter.getAndIncrement());
            task.setCreatedAt(LocalDateTime.now());
        }
        task.setUpdatedAt(LocalDateTime.now());
        tasks.put(task.getId(), task);
        return task;
    }

    public void deleteById(Long id) {
        tasks.remove(id);
    }
}