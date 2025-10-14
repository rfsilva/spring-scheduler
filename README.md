# Task Scheduler

Um sistema de agendamento e execução de tarefas desenvolvido com Java 21 e Spring Boot 3.

## Visão Geral

O Task Scheduler é um sistema que permite agendar e executar diferentes tipos de tarefas de forma automática e programada. O sistema suporta cinco tipos de ações:

1. **Chamadas REST** para endpoints privados
2. **Chamadas SOAP** para serviços privados
3. **Execução de programas batch em plataforma baixa** (ex. jobs)
4. **Execução de programas batch em plataforma alta** (ex. transações COBOL em mainframe)
5. **Envio de mensagens** para filas ou tópicos de mensageria

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- Quartz Scheduler
- MySQL
- Flyway para migração de banco de dados
- Docker e Docker Compose

## Arquitetura

O sistema é baseado em uma arquitetura de microserviços, onde cada tarefa agendada é autônoma e gerenciada individualmente pelo Quartz Scheduler. As principais características são:

- **Desacoplamento**: Cada tarefa é independente e não depende de um job centralizado
- **Persistência**: Todas as tarefas e seus agendamentos são armazenados no banco de dados
- **Escalabilidade**: O sistema pode ser escalado horizontalmente para lidar com um grande número de tarefas
- **Resiliência**: Mecanismos de retry e tratamento de falhas são implementados para cada tipo de tarefa
- **Rastreabilidade**: Histórico completo de execuções para auditoria e monitoramento

## Estrutura do Projeto

```
task-scheduler/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── taskscheduler/
│   │   │           ├── config/         # Configurações do Spring e Quartz
│   │   │           ├── controller/     # Controladores REST
│   │   │           ├── dto/            # Objetos de transferência de dados
│   │   │           ├── enums/          # Enumerações
│   │   │           ├── exception/      # Exceções personalizadas
│   │   │           ├── model/          # Entidades JPA
│   │   │           ├── repository/     # Repositórios Spring Data
│   │   │           ├── service/        # Serviços de negócio
│   │   │           │   └── executor/   # Executores de tarefas
│   │   │           └── util/           # Classes utilitárias
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/          # Scripts de migração Flyway
│   │       ├── application.yml         # Configuração da aplicação
│   │       ├── static/                 # Recursos estáticos
│   │       └── templates/              # Templates
│   └── test/
│       └── java/
│           └── com/
│               └── taskscheduler/      # Testes unitários e de integração
├── postman/                            # Coleção Postman para testes
├── Dockerfile                          # Configuração do Docker
├── docker-compose.yml                  # Configuração do Docker Compose
├── mvnw                                # Maven Wrapper
└── pom.xml                             # Configuração do Maven
```

## Tipos de Tarefas

### 1. Chamadas REST

Permite configurar chamadas HTTP para endpoints REST com as seguintes opções:

- URL
- Método HTTP (GET, POST, PUT, DELETE, PATCH)
- Headers
- Body
- Timeout
- Tipo de autenticação (NONE, BASIC, BEARER, MTLS)
- Política de retry
- Configurações de autenticação OAuth2

### 2. Chamadas SOAP

Permite configurar chamadas para serviços SOAP com as seguintes opções:

- URL do WSDL
- Operação
- Namespace
- SOAP Action
- XML da requisição
- Tipo de autenticação (NONE, BASIC, WSSECURITY, MTLS)
- Timeout
- Credenciais
- Headers customizados

### 3. Batch em Plataforma Baixa

Permite executar comandos e scripts em sistemas operacionais com as seguintes opções:

- Nome do job
- Comando
- Parâmetros
- Diretório de trabalho
- Timeout
- Usuário de execução
- Comandos para sucesso e falha

### 4. Batch em Plataforma Alta

Permite interagir com sistemas mainframe através de diferentes protocolos:

- Tipo de endpoint (CICS, JES, MQ, API)
- ID da transação
- Payload
- Credenciais
- Timeout
- Configurações específicas para cada tipo de endpoint

### 5. Mensageria

Permite enviar mensagens para diferentes sistemas de mensageria:

- Tipo de broker (RABBITMQ, ACTIVEMQ, IBMMQ, KAFKA, SQS)
- Nome do destino (fila ou tópico)
- Payload da mensagem
- URL de conexão
- Tipo de autenticação
- Headers
- Modo de entrega
- Política de retry

## Histórico de Execuções

O sistema mantém um registro detalhado de todas as execuções de tarefas, permitindo:

- Rastreamento completo de cada execução
- Registro de tempo de início e término
- Duração da execução em milissegundos
- Status de conclusão (COMPLETED, FAILED)
- Mensagens de erro detalhadas
- Contagem de retentativas
- Detalhes da resposta ou saída da execução

Esses dados são armazenados no banco de dados e podem ser consultados através da API REST. Além disso, todas as execuções são registradas em logs detalhados, facilitando a integração com sistemas de APM (Application Performance Monitoring).

## Configuração e Execução

### Pré-requisitos

- Java 21
- Docker e Docker Compose
- MySQL (ou use o contêiner Docker fornecido)

### Executando com Docker

1. Clone o repositório
2. Navegue até o diretório do projeto
3. Execute o Docker Compose:

```bash
docker-compose up -d
```

A aplicação estará disponível em `http://localhost:8080/api`

### Executando localmente

1. Clone o repositório
2. Configure um banco de dados MySQL
3. Atualize as configurações de banco de dados em `application.yml`
4. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

## API REST

A API REST fornece endpoints para gerenciar as tarefas agendadas e consultar o histórico de execuções:

### Gerenciamento de Tarefas

- `GET /api/tasks` - Lista todas as tarefas
- `GET /api/tasks/{id}` - Obtém uma tarefa específica
- `POST /api/tasks` - Cria uma nova tarefa
- `PUT /api/tasks/{id}` - Atualiza uma tarefa existente
- `DELETE /api/tasks/{id}` - Remove uma tarefa
- `POST /api/tasks/{id}/activate` - Ativa uma tarefa
- `POST /api/tasks/{id}/deactivate` - Desativa uma tarefa

### Histórico de Execuções

- `GET /api/tasks/{id}/executions` - Obtém o histórico de execuções de uma tarefa
- `GET /api/tasks/{id}/executions/paged` - Obtém o histórico de execuções paginado
- `GET /api/tasks/{id}/executions/date-range` - Obtém o histórico de execuções em um intervalo de datas

## Coleção Postman

Uma coleção Postman está disponível no diretório `postman/` para testar a API. Importe a coleção no Postman e configure a variável `baseUrl` conforme necessário.

## Agendamento de Tarefas

As tarefas são agendadas usando expressões cron. Alguns exemplos:

- `0 0 12 * * ?` - Executa todos os dias às 12h
- `0 15 10 ? * MON-FRI` - Executa às 10:15 de segunda a sexta
- `0 0/5 * * * ?` - Executa a cada 5 minutos
- `0 0 8 1 * ?` - Executa às 8h no primeiro dia de cada mês

## Funcionamento Interno

1. **Cadastro de Tarefas**: As tarefas são cadastradas através da API REST
2. **Agendamento**: Quando uma tarefa é ativada, ela é registrada no Quartz Scheduler
3. **Execução**: No momento agendado, o Quartz dispara a execução da tarefa
4. **Processamento**: O executor específico para o tipo de tarefa realiza a ação configurada
5. **Registro**: O resultado da execução é registrado no banco de dados e nos logs
6. **Monitoramento**: O histórico de execuções pode ser consultado via API REST

## Integração com APM

O sistema foi projetado para facilitar a integração com ferramentas de APM (Application Performance Monitoring). Todos os eventos importantes são registrados em logs estruturados, e o histórico de execuções é armazenado no banco de dados com informações detalhadas sobre:

- Tempo de início e término
- Duração da execução
- Status de conclusão
- Mensagens de erro
- Detalhes da resposta

Isso permite que ferramentas de APM como Datadog, New Relic, Dynatrace ou Prometheus/Grafana possam monitorar o desempenho e a saúde do sistema.

## Contribuição

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Faça commit das suas alterações (`git commit -am 'Adiciona nova feature'`)
4. Faça push para a branch (`git push origin feature/nova-feature`)
5. Crie um novo Pull Request