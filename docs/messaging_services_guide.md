# Guia de Serviços de Mensageria para o Task Scheduler

Este guia explica como utilizar os serviços de mensageria (Kafka e RabbitMQ) configurados no ambiente do Task Scheduler.

## Visão Geral

O Task Scheduler foi configurado para suportar o envio de mensagens para os seguintes sistemas de mensageria:

1. **Kafka** - Sistema de mensageria distribuído baseado em logs
2. **RabbitMQ** - Sistema de mensageria baseado no protocolo AMQP

## Serviços Disponíveis

### Kafka

O serviço Kafka está disponível no ambiente com os seguintes detalhes:

- **Host**: `kafka:9092` (dentro da rede Docker)
- **Host Externo**: `localhost:29092` (para acesso fora do Docker)
- **Interface Web**: http://localhost:8090 (Kafka UI)

#### Tópicos Pré-configurados:

1. **task-notifications** - Para envio de notificações do sistema
2. **task-events** - Para registro de eventos do sistema
3. **task-results** - Para armazenamento de resultados de tarefas

### RabbitMQ

O serviço RabbitMQ está disponível no ambiente com os seguintes detalhes:

- **Host**: `rabbitmq:5672` (dentro da rede Docker)
- **Host Externo**: `localhost:5672` (para acesso fora do Docker)
- **Interface Web**: http://localhost:15672 (RabbitMQ Management)
  - **Usuário**: admin
  - **Senha**: admin123

#### Filas e Exchanges Pré-configurados:

1. **task-queue** - Fila para processamento de tarefas
   - Vinculada ao exchange `task-exchange` com routing key `task-routing-key`

2. **notification-queue** - Fila para notificações
   - Vinculada ao exchange `notification-exchange` com routing key `notification.#`

## Configuração de Tarefas no Task Scheduler

### Parâmetros para Kafka

Para configurar uma tarefa que envia mensagens para o Kafka, utilize os seguintes parâmetros:

- **Broker Type**: `KAFKA`
- **Connection URL**: `kafka:9092` (se estiver dentro da rede Docker) ou `localhost:29092` (se estiver fora)
- **Destination Name**: Nome do tópico (ex: `task-notifications`)
- **Auth Type**: `NONE` (para o ambiente de teste)
- **Headers**: JSON com cabeçalhos (ex: `{"Content-Type": "application/json"}`)
- **Message Payload**: Conteúdo da mensagem em formato JSON ou texto

### Parâmetros para RabbitMQ

Para configurar uma tarefa que envia mensagens para o RabbitMQ, utilize os seguintes parâmetros:

- **Broker Type**: `RABBITMQ`
- **Connection URL**: `amqp://admin:admin123@rabbitmq:5672` (dentro da rede Docker) ou `amqp://admin:admin123@localhost:5672` (fora)
- **Destination Name**: 
  - Para enviar para uma fila: Nome da fila (ex: `task-queue`)
  - Para enviar para um exchange: `nome-do-exchange/routing-key` (ex: `notification-exchange/notification.system`)
- **Auth Type**: `BASIC`
- **Headers**: JSON com cabeçalhos (ex: `{"Content-Type": "application/json", "X-Priority": "high"}`)
- **Message Payload**: Conteúdo da mensagem em formato JSON ou texto
- **Delivery Mode**: `PERSISTENT` (recomendado para garantir entrega)

## Exemplos de Configuração

### Exemplo de Tarefa para Kafka

```json
{
  "name": "Envio de Notificação Kafka",
  "description": "Envia notificação para o tópico task-notifications do Kafka",
  "taskType": "MESSAGING",
  "status": "ACTIVE",
  "cronExpression": "0 */5 * * * ?",
  "messagingTaskConfig": {
    "brokerType": "KAFKA",
    "destinationName": "task-notifications",
    "messagePayload": "{\"notification_id\": \"NOTIF-${uuid()}\", \"timestamp\": \"${now()}\", \"level\": \"INFO\", \"message\": \"Notificação periódica do sistema\", \"source\": \"task-scheduler\"}",
    "connectionUrl": "kafka:9092",
    "authType": "NONE",
    "headers": "{\"Content-Type\": \"application/json\", \"X-Source\": \"task-scheduler\"}",
    "deliveryMode": "PERSISTENT",
    "retryPolicy": "{\"maxAttempts\": 3, \"backoffPolicy\": \"EXPONENTIAL\"}"
  }
}
```

### Exemplo de Tarefa para RabbitMQ

```json
{
  "name": "Envio para Fila RabbitMQ",
  "description": "Envia mensagem para a fila task-queue do RabbitMQ",
  "taskType": "MESSAGING",
  "status": "ACTIVE",
  "cronExpression": "0 */10 * * * ?",
  "messagingTaskConfig": {
    "brokerType": "RABBITMQ",
    "destinationName": "task-queue",
    "messagePayload": "{\"task_id\": \"TASK-${uuid()}\", \"timestamp\": \"${now()}\", \"action\": \"PROCESS\", \"data\": {\"customer_id\": \"CUST-123\", \"order_id\": \"ORD-456\", \"amount\": 1250.75}}",
    "connectionUrl": "amqp://admin:admin123@rabbitmq:5672",
    "authType": "BASIC",
    "headers": "{\"Content-Type\": \"application/json\", \"X-Priority\": \"high\"}",
    "deliveryMode": "PERSISTENT",
    "retryPolicy": "{\"maxAttempts\": 3, \"backoffPolicy\": \"FIXED\"}"
  }
}
```

## Variáveis Dinâmicas

O Task Scheduler suporta algumas variáveis dinâmicas que podem ser usadas no payload das mensagens:

- `${uuid()}` - Gera um UUID único
- `${now()}` - Insere a data/hora atual no formato ISO
- `${date(formato)}` - Insere a data atual no formato especificado (ex: `${date(yyyy-MM-dd)}`)

## Monitoramento

### Kafka

Para monitorar as mensagens enviadas para o Kafka, acesse a interface web do Kafka UI:
- URL: http://localhost:8090
- Navegue até o tópico desejado para visualizar as mensagens

### RabbitMQ

Para monitorar as mensagens enviadas para o RabbitMQ, acesse a interface de gerenciamento:
- URL: http://localhost:15672
- Usuário: admin
- Senha: admin123
- Navegue até a seção "Queues" para visualizar as filas e suas mensagens