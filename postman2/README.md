# Task Scheduler API - Postman Collection

Esta pasta contém arquivos de configuração do Postman para testar a API do Task Scheduler.

## Arquivos

- `Task_Scheduler_API.postman_collection.json`: Coleção com todas as chamadas possíveis aos endpoints da API
- `Task_Scheduler_Environment.postman_environment.json`: Variáveis de ambiente para facilitar as chamadas

## Como usar

1. Importe a coleção e o ambiente no Postman
2. Selecione o ambiente "Task Scheduler Environment"
3. Ajuste as variáveis de ambiente conforme necessário:
   - `baseUrl`: URL base da API (padrão: http://localhost:8080/api)
   - `taskId`: ID da tarefa para operações que exigem um ID específico
   - `startDateTime` e `endDateTime`: Datas para filtrar execuções de tarefas

## Endpoints disponíveis

### Tarefas Agendadas (Scheduled Tasks)

- **GET /tasks**: Listar todas as tarefas
- **GET /tasks/{id}**: Obter tarefa por ID
- **POST /tasks**: Criar nova tarefa
- **PUT /tasks/{id}**: Atualizar tarefa existente
- **DELETE /tasks/{id}**: Excluir tarefa
- **POST /tasks/{id}/activate**: Ativar tarefa
- **POST /tasks/{id}/deactivate**: Desativar tarefa

### Execuções de Tarefas (Task Executions)

- **GET /tasks/{taskId}/executions**: Obter histórico de execuções de uma tarefa
- **GET /tasks/{taskId}/executions/paged**: Obter histórico de execuções paginado
- **GET /tasks/{taskId}/executions/date-range**: Obter histórico de execuções por intervalo de datas

### Templates de Tarefas

A coleção também inclui exemplos para criar diferentes tipos de tarefas:
- REST API
- SOAP API
- Low Platform Batch
- High Platform Batch
- Messaging

## Swagger UI

A API também está configurada com Swagger UI, que pode ser acessada em:
- http://localhost:8080/api/swagger-ui.html

## Documentação OpenAPI

A documentação OpenAPI está disponível em:
- http://localhost:8080/api/v3/api-docs