# Ambiente de Teste para Task Scheduler

Este repositório contém um ambiente de teste controlado para o Task Scheduler, incluindo:

1. Uma API REST Java simples sem segurança para testes
2. Configuração Docker Compose para execução do ambiente
3. Massa de dados de teste para validação do front-end e agendamentos

## Componentes

### REST API Unsecure

Uma API REST Java simples sem segurança que fornece endpoints para:
- Gerenciamento de tarefas (CRUD)
- Endpoints de eco para validação de chamadas
- Endpoints de status para monitoramento

A API está disponível na porta 8081 e não requer autenticação.

### Massa de Dados de Teste

O arquivo `test_data_updated.sql` contém uma massa de dados de teste que inclui:
- 7 tarefas agendadas para APIs públicas (4 REST e 3 SOAP)
- 6 tarefas agendadas para a API REST local
- 18 execuções de tarefas para histórico
- Diferentes estados de tarefas (ACTIVE, INACTIVE)
- Diferentes resultados de execução (SUCCESS, FAILED)

## Executando o Ambiente de Teste

### Pré-requisitos

- Docker e Docker Compose instalados
- Java 17 (para execução local)
- Maven (para execução local)

### Passos para Execução

1. Inicie o ambiente com Docker Compose:

```bash
docker-compose up -d
```

2. Importe a massa de dados de teste:

```bash
# No Windows
import_test_data.bat

# No Linux/Mac
./import_test_data.sh
```

3. Acesse o front-end do Task Scheduler (porta configurada no seu ambiente)

4. Acesse a API REST de teste:

```
http://localhost:8081/api/tasks
```

## Testando os Endpoints

### Endpoints da API REST Local

- **GET /api/tasks** - Lista todas as tarefas
- **GET /api/tasks/{id}** - Obtém uma tarefa específica por ID
- **POST /api/tasks** - Cria uma nova tarefa
- **GET /api/echo?param=value** - Endpoint de eco para GET
- **POST /api/echo** - Endpoint de eco para POST
- **GET /api/status** - Status da aplicação
- **GET /api/health** - Saúde da aplicação

### Exemplos de Chamadas

Consulte o arquivo `endpoint_testing_examples.md` para exemplos de como chamar os endpoints.

## Tarefas Agendadas

As tarefas agendadas no Task Scheduler incluem:

### Tarefas para APIs Públicas

1. Consulta de CEP (ViaCEP)
2. Consulta de Feriados Nacionais (Brasil API)
3. Cotação de Moedas (Awesome API)
4. Consulta de Clima (OpenWeatherMap)
5. Calculadora SOAP
6. Conversão de Temperatura SOAP
7. Número por Extenso SOAP

### Tarefas para API REST Local

1. Listar Tarefas API Local
2. Consultar Tarefa por ID
3. Criar Nova Tarefa
4. Echo GET API Local
5. Echo POST API Local
6. Verificar Status API Local

## Arquivos de Documentação

- **test_data_README.md** - Documentação sobre a massa de dados de teste
- **endpoint_testing_examples.md** - Exemplos de como testar os endpoints
- **cron_examples.md** - Exemplos de expressões cron para testes
- **frontend_testing_guide.md** - Guia para testar o front-end
- **rest-api-unsecure/README.md** - Documentação da API REST de teste

## Observações

- A API REST local está configurada para ser acessada pelo Task Scheduler através do nome do container Docker (`rest-api-unsecure`)
- Para testes locais, substitua `rest-api-unsecure` por `localhost` nas URLs
- Alguns endpoints públicos podem ter limitações de taxa de requisições