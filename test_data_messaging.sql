-- Massa de dados de teste para tarefas de mensageria (Kafka e RabbitMQ)
-- Este script cria tarefas agendadas para enviar mensagens para Kafka e RabbitMQ

-- Inserir tarefas agendadas para mensageria

-- 1. Kafka - Envio de notificação para o tópico task-notifications
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(100, NOW(), NOW(), 'Envio de Notificação Kafka', 'Envia notificação para o tópico task-notifications do Kafka', 'MESSAGING', 'ACTIVE', '0 */5 * * * ?', NOW() + INTERVAL '5 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Envio periódico de notificações via Kafka"}');

INSERT INTO messaging_task_configs (id, created_at, updated_at, scheduled_task_id, broker_type, destination_name, message_payload, connection_url, auth_type, headers, delivery_mode, retry_policy)
VALUES 
(1, NOW(), NOW(), 100, 'KAFKA', 'task-notifications', '{"notification_id": "NOTIF-${uuid()}", "timestamp": "${now()}", "level": "INFO", "message": "Notificação periódica do sistema", "source": "task-scheduler"}', 'kafka:9092', 'NONE', '{"Content-Type": "application/json", "X-Source": "task-scheduler"}', 'PERSISTENT', '{"maxAttempts": 3, "backoffPolicy": "EXPONENTIAL"}');

-- 2. Kafka - Envio de evento para o tópico task-events
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(101, NOW(), NOW(), 'Envio de Evento Kafka', 'Envia evento para o tópico task-events do Kafka', 'MESSAGING', 'ACTIVE', '0 0 */1 * * ?', NOW() + INTERVAL '1 HOUR', NULL, NULL, NULL, 0, 3, 60, '{"description": "Envio horário de eventos via Kafka"}');

INSERT INTO messaging_task_configs (id, created_at, updated_at, scheduled_task_id, broker_type, destination_name, message_payload, connection_url, auth_type, headers, delivery_mode, retry_policy)
VALUES 
(2, NOW(), NOW(), 101, 'KAFKA', 'task-events', '{"event_id": "EVT-${uuid()}", "timestamp": "${now()}", "type": "SYSTEM_CHECK", "status": "OK", "details": {"cpu_usage": "45%", "memory_usage": "60%", "disk_usage": "35%"}}', 'kafka:9092', 'NONE', '{"Content-Type": "application/json", "X-Source": "task-scheduler"}', 'PERSISTENT', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}');

-- 3. Kafka - Envio de resultado para o tópico task-results
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(102, NOW(), NOW(), 'Envio de Resultado Kafka', 'Envia resultado para o tópico task-results do Kafka', 'MESSAGING', 'ACTIVE', '0 30 23 * * ?', NOW() + INTERVAL '1 DAY', NULL, NULL, NULL, 0, 3, 60, '{"description": "Envio diário de resultados via Kafka"}');

INSERT INTO messaging_task_configs (id, created_at, updated_at, scheduled_task_id, broker_type, destination_name, message_payload, connection_url, auth_type, headers, delivery_mode, retry_policy)
VALUES 
(3, NOW(), NOW(), 102, 'KAFKA', 'task-results', '{"result_id": "RES-${uuid()}", "timestamp": "${now()}", "date": "${date(yyyy-MM-dd)}", "summary": {"total_tasks": 120, "completed": 115, "failed": 5}, "details": "Relatório diário de execução de tarefas"}', 'kafka:9092', 'NONE', '{"Content-Type": "application/json", "X-Source": "task-scheduler"}', 'PERSISTENT', '{"maxAttempts": 3, "backoffPolicy": "EXPONENTIAL"}');

-- 4. RabbitMQ - Envio de mensagem para a fila task-queue
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(103, NOW(), NOW(), 'Envio para Fila RabbitMQ', 'Envia mensagem para a fila task-queue do RabbitMQ', 'MESSAGING', 'ACTIVE', '0 */10 * * * ?', NOW() + INTERVAL '10 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Envio periódico de mensagens para fila RabbitMQ"}');

INSERT INTO messaging_task_configs (id, created_at, updated_at, scheduled_task_id, broker_type, destination_name, message_payload, connection_url, auth_type, headers, delivery_mode, retry_policy)
VALUES 
(4, NOW(), NOW(), 103, 'RABBITMQ', 'task-queue', '{"task_id": "TASK-${uuid()}", "timestamp": "${now()}", "action": "PROCESS", "data": {"customer_id": "CUST-123", "order_id": "ORD-456", "amount": 1250.75}}', 'amqp://admin:admin123@rabbitmq:5672', 'BASIC', '{"Content-Type": "application/json", "X-Priority": "high"}', 'PERSISTENT', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}');

-- 5. RabbitMQ - Envio de notificação para o exchange notification-exchange
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(104, NOW(), NOW(), 'Envio para Exchange RabbitMQ', 'Envia notificação para o exchange notification-exchange do RabbitMQ', 'MESSAGING', 'ACTIVE', '0 15 */2 * * ?', NOW() + INTERVAL '2 HOUR', NULL, NULL, NULL, 0, 3, 60, '{"description": "Envio periódico de notificações para exchange RabbitMQ"}');

INSERT INTO messaging_task_configs (id, created_at, updated_at, scheduled_task_id, broker_type, destination_name, message_payload, connection_url, auth_type, headers, delivery_mode, retry_policy)
VALUES 
(5, NOW(), NOW(), 104, 'RABBITMQ', 'notification-exchange/notification.system', '{"notification_id": "NOTIF-${uuid()}", "timestamp": "${now()}", "type": "SYSTEM", "message": "Verificação de sistema concluída", "details": "Todos os serviços estão operacionais"}', 'amqp://admin:admin123@rabbitmq:5672', 'BASIC', '{"Content-Type": "application/json", "X-Source": "task-scheduler"}', 'PERSISTENT', '{"maxAttempts": 3, "backoffPolicy": "EXPONENTIAL"}');

-- 6. RabbitMQ - Envio de alerta para o exchange notification-exchange (inativo)
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(105, NOW(), NOW(), 'Envio de Alerta RabbitMQ', 'Envia alerta para o exchange notification-exchange do RabbitMQ', 'MESSAGING', 'INACTIVE', '0 */30 * * * ?', NULL, NULL, NULL, NULL, 0, 3, 60, '{"description": "Envio de alertas críticos via RabbitMQ"}');

INSERT INTO messaging_task_configs (id, created_at, updated_at, scheduled_task_id, broker_type, destination_name, message_payload, connection_url, auth_type, headers, delivery_mode, retry_policy)
VALUES 
(6, NOW(), NOW(), 105, 'RABBITMQ', 'notification-exchange/notification.alert', '{"alert_id": "ALERT-${uuid()}", "timestamp": "${now()}", "level": "CRITICAL", "message": "Alerta de segurança detectado", "source": "security-monitor"}', 'amqp://admin:admin123@rabbitmq:5672', 'BASIC', '{"Content-Type": "application/json", "X-Priority": "critical"}', 'PERSISTENT', '{"maxAttempts": 5, "backoffPolicy": "EXPONENTIAL"}');

-- Inserir execuções de tarefas para histórico

-- Execuções para a tarefa de Envio de Notificação Kafka
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(100, NOW() - INTERVAL '15 MINUTE', NOW() - INTERVAL '15 MINUTE', 100, NOW() - INTERVAL '15 MINUTE', NOW() - INTERVAL '15 MINUTE', 'SUCCESS', 'Message sent to Kafka topic: task-notifications', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(101, NOW() - INTERVAL '10 MINUTE', NOW() - INTERVAL '10 MINUTE', 100, NOW() - INTERVAL '10 MINUTE', NOW() - INTERVAL '10 MINUTE', 'SUCCESS', 'Message sent to Kafka topic: task-notifications', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(102, NOW() - INTERVAL '5 MINUTE', NOW() - INTERVAL '5 MINUTE', 100, NOW() - INTERVAL '5 MINUTE', NOW() - INTERVAL '5 MINUTE', 'SUCCESS', 'Message sent to Kafka topic: task-notifications', NULL, 0);

-- Execuções para a tarefa de Envio de Evento Kafka
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(103, NOW() - INTERVAL '3 HOUR', NOW() - INTERVAL '3 HOUR', 101, NOW() - INTERVAL '3 HOUR', NOW() - INTERVAL '3 HOUR', 'SUCCESS', 'Message sent to Kafka topic: task-events', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(104, NOW() - INTERVAL '2 HOUR', NOW() - INTERVAL '2 HOUR', 101, NOW() - INTERVAL '2 HOUR', NOW() - INTERVAL '2 HOUR', 'SUCCESS', 'Message sent to Kafka topic: task-events', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(105, NOW() - INTERVAL '1 HOUR', NOW() - INTERVAL '1 HOUR', 101, NOW() - INTERVAL '1 HOUR', NOW() - INTERVAL '1 HOUR', 'SUCCESS', 'Message sent to Kafka topic: task-events', NULL, 0);

-- Execuções para a tarefa de Envio para Fila RabbitMQ
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(106, NOW() - INTERVAL '30 MINUTE', NOW() - INTERVAL '30 MINUTE', 103, NOW() - INTERVAL '30 MINUTE', NOW() - INTERVAL '30 MINUTE', 'SUCCESS', 'Message sent to RabbitMQ destination: task-queue', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(107, NOW() - INTERVAL '20 MINUTE', NOW() - INTERVAL '20 MINUTE', 103, NOW() - INTERVAL '20 MINUTE', NOW() - INTERVAL '20 MINUTE', 'SUCCESS', 'Message sent to RabbitMQ destination: task-queue', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(108, NOW() - INTERVAL '10 MINUTE', NOW() - INTERVAL '10 MINUTE', 103, NOW() - INTERVAL '10 MINUTE', NOW() - INTERVAL '10 MINUTE', 'SUCCESS', 'Message sent to RabbitMQ destination: task-queue', NULL, 0);

-- Execuções para a tarefa de Envio para Exchange RabbitMQ
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(109, NOW() - INTERVAL '6 HOUR', NOW() - INTERVAL '6 HOUR', 104, NOW() - INTERVAL '6 HOUR', NOW() - INTERVAL '6 HOUR', 'SUCCESS', 'Message sent to RabbitMQ destination: notification-exchange/notification.system', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(110, NOW() - INTERVAL '4 HOUR', NOW() - INTERVAL '4 HOUR', 104, NOW() - INTERVAL '4 HOUR', NOW() - INTERVAL '4 HOUR', 'SUCCESS', 'Message sent to RabbitMQ destination: notification-exchange/notification.system', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(111, NOW() - INTERVAL '2 HOUR', NOW() - INTERVAL '2 HOUR', 104, NOW() - INTERVAL '2 HOUR', NOW() - INTERVAL '2 HOUR', 'SUCCESS', 'Message sent to RabbitMQ destination: notification-exchange/notification.system', NULL, 0);