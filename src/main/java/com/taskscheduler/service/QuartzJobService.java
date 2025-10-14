package com.taskscheduler.service;

import com.taskscheduler.model.ScheduledTask;
import com.taskscheduler.service.executor.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuartzJobService {

    private final Scheduler scheduler;

    /**
     * Agenda uma tarefa no Quartz. Se a tarefa já estiver agendada, ela será atualizada.
     *
     * @param task A tarefa a ser agendada
     * @throws SchedulerException Se ocorrer um erro ao agendar a tarefa
     */
    public void scheduleJob(ScheduledTask task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(getJobKeyName(task), getJobKeyGroup(task));
        
        // Verifica se o job já existe
        if (scheduler.checkExists(jobKey)) {
            // Se já existe, remove para recriar com as configurações atualizadas
            log.info("Job already exists for task: {} (ID: {}). Updating...", task.getName(), task.getId());
            scheduler.deleteJob(jobKey);
        }
        
        JobDetail jobDetail = createJobDetail(task);
        Trigger trigger = createTrigger(task);
        
        scheduler.scheduleJob(jobDetail, trigger);
        log.info("Scheduled task: {} with cron expression: {}", task.getName(), task.getCronExpression());
    }

    /**
     * Remove uma tarefa do Quartz.
     *
     * @param task A tarefa a ser removida
     * @throws SchedulerException Se ocorrer um erro ao remover a tarefa
     */
    public void unscheduleJob(ScheduledTask task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(getJobKeyName(task), getJobKeyGroup(task));
        
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
            log.info("Unscheduled task: {}", task.getName());
        } else {
            log.warn("Attempted to unschedule non-existent task: {} (ID: {})", task.getName(), task.getId());
        }
    }

    /**
     * Verifica se uma tarefa já está agendada no Quartz.
     *
     * @param task A tarefa a ser verificada
     * @return true se a tarefa já estiver agendada, false caso contrário
     * @throws SchedulerException Se ocorrer um erro ao verificar a tarefa
     */
    public boolean isJobScheduled(ScheduledTask task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(getJobKeyName(task), getJobKeyGroup(task));
        return scheduler.checkExists(jobKey);
    }

    /**
     * Calcula o próximo horário de execução de uma tarefa com base na expressão cron.
     *
     * @param cronExpression A expressão cron
     * @return O próximo horário de execução
     */
    public LocalDateTime calculateNextExecutionTime(String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            Date nextValidTimeAfter = cron.getNextValidTimeAfter(new Date());
            return nextValidTimeAfter.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            log.error("Error calculating next execution time: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Cria um JobDetail para a tarefa.
     *
     * @param task A tarefa
     * @return O JobDetail criado
     */
    private JobDetail createJobDetail(ScheduledTask task) {
        Class<? extends Job> jobClass = getJobClassForTaskType(task);
        
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("taskId", task.getId());
        
        return JobBuilder.newJob(jobClass)
                .withIdentity(getJobKeyName(task), getJobKeyGroup(task))
                .withDescription(task.getDescription())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    /**
     * Cria um Trigger para a tarefa.
     *
     * @param task A tarefa
     * @return O Trigger criado
     */
    private Trigger createTrigger(ScheduledTask task) {
        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKeyName(task), getTriggerKeyGroup(task))
                .withDescription(task.getDescription())
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression())
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }

    /**
     * Obtém a classe de Job apropriada para o tipo de tarefa.
     *
     * @param task A tarefa
     * @return A classe de Job
     */
    private Class<? extends Job> getJobClassForTaskType(ScheduledTask task) {
        switch (task.getTaskType()) {
            case REST_CALL:
                return RestTaskExecutor.class;
            case SOAP_CALL:
                return SoapTaskExecutor.class;
            case LOW_PLATFORM_BATCH:
                return LowPlatformBatchExecutor.class;
            case HIGH_PLATFORM_BATCH:
                return HighPlatformBatchExecutor.class;
            case MESSAGING:
                return MessagingTaskExecutor.class;
            default:
                throw new IllegalArgumentException("Unsupported task type: " + task.getTaskType());
        }
    }

    private String getJobKeyName(ScheduledTask task) {
        return "job_" + task.getId();
    }

    private String getJobKeyGroup(ScheduledTask task) {
        return "task_group_" + task.getTaskType();
    }

    private String getTriggerKeyName(ScheduledTask task) {
        return "trigger_" + task.getId();
    }

    private String getTriggerKeyGroup(ScheduledTask task) {
        return "task_trigger_group_" + task.getTaskType();
    }
}