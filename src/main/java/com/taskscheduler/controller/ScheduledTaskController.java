package com.taskscheduler.controller;

import com.taskscheduler.dto.ScheduledTaskDTO;
import com.taskscheduler.enums.TaskStatus;
import com.taskscheduler.service.ScheduledTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Scheduled Tasks", description = "API para gerenciamento de tarefas agendadas")
public class ScheduledTaskController {

    private final ScheduledTaskService scheduledTaskService;

    @Operation(summary = "Listar todas as tarefas", description = "Retorna uma lista de todas as tarefas agendadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas recuperada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<ScheduledTaskDTO>> getAllTasks() {
        return ResponseEntity.ok(scheduledTaskService.getAllTasks());
    }

    @Operation(summary = "Obter tarefa por ID", description = "Retorna uma tarefa específica pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ScheduledTaskDTO> getTaskById(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(scheduledTaskService.getTaskById(id));
    }

    @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa agendada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<ScheduledTaskDTO> createTask(
            @Parameter(description = "Dados da tarefa", required = true)
            @Valid @RequestBody ScheduledTaskDTO taskDTO) {
        return new ResponseEntity<>(scheduledTaskService.createTask(taskDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar tarefa", description = "Atualiza uma tarefa existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ScheduledTaskDTO> updateTask(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados da tarefa", required = true)
            @Valid @RequestBody ScheduledTaskDTO taskDTO) {
        return ResponseEntity.ok(scheduledTaskService.updateTask(id, taskDTO));
    }

    @Operation(summary = "Excluir tarefa", description = "Remove uma tarefa existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable Long id) {
        scheduledTaskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar tarefa", description = "Ativa uma tarefa (muda o status para ACTIVE)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa ativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/{id}/activate")
    public ResponseEntity<ScheduledTaskDTO> activateTask(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(scheduledTaskService.activateTask(id));
    }

    @Operation(summary = "Desativar tarefa", description = "Desativa uma tarefa (muda o status para INACTIVE)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa desativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ScheduledTaskDTO> deactivateTask(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(scheduledTaskService.deactivateTask(id));
    }
}