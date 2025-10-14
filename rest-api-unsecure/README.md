# REST API Unsecure

Esta é uma API REST Java simples sem segurança para testes do Task Scheduler. A API fornece endpoints para gerenciamento de tarefas e endpoints de eco para validação de chamadas.

## Endpoints Disponíveis

### Gerenciamento de Tarefas

- **GET /api/tasks** - Lista todas as tarefas
- **GET /api/tasks/{id}** - Obtém uma tarefa específica por ID
- **POST /api/tasks** - Cria uma nova tarefa
- **PUT /api/tasks/{id}** - Atualiza uma tarefa existente
- **DELETE /api/tasks/{id}** - Remove uma tarefa

### Endpoints de Eco

- **GET /api/echo** - Retorna os parâmetros enviados na requisição
- **POST /api/echo** - Retorna o corpo enviado na requisição
- **PUT /api/echo** - Retorna o corpo enviado na requisição
- **DELETE /api/echo** - Retorna uma confirmação de recebimento

### Endpoints de Status

- **GET /api/status** - Retorna informações sobre o status da aplicação
- **GET /api/health** - Retorna informações sobre a saúde da aplicação

## Exemplos de Uso

### Listar todas as tarefas

```bash
curl -X GET http://localhost:8081/api/tasks
```

### Obter uma tarefa específica

```bash
curl -X GET http://localhost:8081/api/tasks/1
```

### Criar uma nova tarefa

```bash
curl -X POST http://localhost:8081/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Nova Tarefa", "description": "Descrição da nova tarefa", "status": "PENDING", "priority": 1}'
```

### Atualizar uma tarefa existente

```bash
curl -X PUT http://localhost:8081/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "Tarefa Atualizada", "description": "Descrição atualizada", "status": "IN_PROGRESS", "priority": 2}'
```

### Remover uma tarefa

```bash
curl -X DELETE http://localhost:8081/api/tasks/1
```

### Testar o endpoint de eco (GET)

```bash
curl -X GET "http://localhost:8081/api/echo?param1=value1&param2=value2"
```

### Testar o endpoint de eco (POST)

```bash
curl -X POST http://localhost:8081/api/echo \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello", "timestamp": "2023-05-20T10:00:00"}'
```

### Verificar o status da aplicação

```bash
curl -X GET http://localhost:8081/api/status
```

### Verificar a saúde da aplicação

```bash
curl -X GET http://localhost:8081/api/health
```

## Executando a API

### Com Docker

```bash
docker-compose up rest-api-unsecure
```

### Localmente

```bash
cd rest-api-unsecure
./mvnw spring-boot:run
```

## Integração com o Task Scheduler

Esta API foi criada para ser usada com o Task Scheduler para testes em ambiente controlado. As tarefas agendadas no Task Scheduler podem chamar os endpoints desta API para validar o funcionamento do agendador.