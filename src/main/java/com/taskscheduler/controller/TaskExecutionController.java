package com.taskscheduler.controller;

import com.taskscheduler.dto.TaskExecutionDTO;
import com.taskscheduler.service.TaskExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Task Executions", description = "API para consulta de histórico de execuções de tarefas")
public class TaskExecutionController {

    private final TaskExecutionService taskExecutionService;

    @Operation(summary = "Obter histórico de execuções", description = "Retorna o histórico completo de execuções de uma tarefa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<TaskExecutionDTO>> getTaskExecutionHistory(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable Long taskId) {
        return ResponseEntity.ok(taskExecutionService.getTaskExecutionHistory(taskId));
    }

    @Operation(summary = "Obter histórico de execuções paginado", description = "Retorna o histórico de execuções de uma tarefa com paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico paginado recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<TaskExecutionDTO>> getTaskExecutionHistoryPaged(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable Long taskId,
            @Parameter(description = "Informações de paginação (page, size, sort)")
            Pageable pageable) {
        return ResponseEntity.ok(taskExecutionService.getTaskExecutionHistoryPaged(taskId, pageable));
    }

    @Operation(summary = "Obter histórico por intervalo de datas", description = "Retorna o histórico de execuções de uma tarefa dentro de um intervalo de datas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico filtrado recuperado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros de data inválidos"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<TaskExecutionDTO>> getTaskExecutionHistoryByDateRange(
            @Parameter(description = "ID da tarefa", required = true)
            @PathVariable Long taskId,
            @Parameter(description = "Data de início (formato ISO: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Data de fim (formato ISO: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(taskExecutionService.getTaskExecutionHistoryByDateRange(taskId, start, end));
    }
}