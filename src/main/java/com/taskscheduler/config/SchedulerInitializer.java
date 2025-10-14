package com.taskscheduler.config;

import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.model.ScheduledTask;
import com.taskscheduler.repository.ScheduledTaskRepository;
import com.taskscheduler.service.QuartzJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Componente responsável por inicializar o agendador de tarefas quando a aplicação é iniciada.
 * Carrega todas as tarefas ativas do banco de dados e as agenda no Quartz.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ScheduledTaskRepository scheduledTaskRepository;
    private final QuartzJobService quartzJobService;

    /**
     * Este método é executado quando a aplicação está totalmente inicializada e pronta para receber requisições.
     * Carrega todas as tarefas ativas do banco de dados e as agenda no Quartz.
     *
     * @param event O evento de inicialização da aplicação
     */
    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("=== Initializing Task Scheduler ===");
        log.info("Loading active tasks from database...");
        
        try {
            // Busca todas as tarefas com status ACTIVE no banco de dados
            List<ScheduledTask> activeTasks = scheduledTaskRepository.findByStatus(TaskStatus.ACTIVE);
            log.info("Found {} active tasks to schedule", activeTasks.size());
            
            int scheduledCount = 0;
            int errorCount = 0;
            
            // Para cada tarefa ativa, agenda no Quartz
            for (ScheduledTask task : activeTasks) {
                try {
                    log.debug("Processing task: {} (ID: {})", task.getName(), task.getId());
                    
                    // Verifica se a tarefa já está agendada
                    boolean alreadyScheduled = false;
                    try {
                        alreadyScheduled = quartzJobService.isJobScheduled(task);
                    } catch (Exception e) {
                        log.warn("Error checking if job is already scheduled: {}", e.getMessage());
                    }
                    
                    if (alreadyScheduled) {
                        log.debug("Task already scheduled: {} (ID: {})", task.getName(), task.getId());
                    } else {
                        // Agenda a tarefa
                        quartzJobService.scheduleJob(task);
                        scheduledCount++;
                        
                        // Atualiza a próxima execução
                        LocalDateTime nextExecution = quartzJobService.calculateNextExecutionTime(task.getCronExpression());
                        task.setNextExecution(nextExecution);
                        scheduledTaskRepository.save(task);
                        
                        log.info("Scheduled task: {} (ID: {}) - Next execution: {}", 
                                task.getName(), task.getId(), nextExecution);
                    }
                } catch (SchedulerException e) {
                    errorCount++;
                    log.error("Failed to schedule task: {} (ID: {}). Error: {}", 
                            task.getName(), task.getId(), e.getMessage(), e);
                }
            }
            
            log.info("=== Scheduler initialization completed ===");
            log.info("Total tasks: {}, Successfully scheduled: {}, Errors: {}", 
                    activeTasks.size(), scheduledCount, errorCount);
        } catch (Exception e) {
            log.error("Critical error during scheduler initialization", e);
        }
    }
}