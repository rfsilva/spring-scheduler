-- Adicionar tarefas agendadas para a API REST não segura

-- REST API Unsecure - GET Tasks
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(8, NOW(), NOW(), 'Listar Tarefas API Local', 'Consulta de tarefas na API REST local não segura', 'REST_CALL', 'ACTIVE', '0 */5 * * * ?', NOW() + INTERVAL '5 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Consulta de tarefas na API REST local"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(5, NOW(), NOW(), 8, 'http://rest-api-unsecure:8081/api/tasks', 'GET', '{"Content-Type": "application/json", "Accept": "application/json"}', NULL, 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);

-- REST API Unsecure - GET Task by ID
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(9, NOW(), NOW(), 'Consultar Tarefa por ID', 'Consulta de tarefa específica na API REST local não segura', 'REST_CALL', 'ACTIVE', '0 */10 * * * ?', NOW() + INTERVAL '10 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Consulta de tarefa específica na API REST local"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(6, NOW(), NOW(), 9, 'http://rest-api-unsecure:8081/api/tasks/1', 'GET', '{"Content-Type": "application/json", "Accept": "application/json"}', NULL, 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);

-- REST API Unsecure - POST Task
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(10, NOW(), NOW(), 'Criar Nova Tarefa', 'Criação de nova tarefa na API REST local não segura', 'REST_CALL', 'ACTIVE', '0 0 */1 * * ?', NOW() + INTERVAL '1 HOUR', NULL, NULL, NULL, 0, 3, 60, '{"description": "Criação de nova tarefa na API REST local"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(7, NOW(), NOW(), 10, 'http://rest-api-unsecure:8081/api/tasks', 'POST', '{"Content-Type": "application/json", "Accept": "application/json"}', '{"title": "Tarefa Agendada", "description": "Tarefa criada pelo agendador", "status": "PENDING", "priority": 1}', 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);

-- REST API Unsecure - Echo GET
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(11, NOW(), NOW(), 'Echo GET API Local', 'Teste de eco GET na API REST local não segura', 'REST_CALL', 'ACTIVE', '0 */15 * * * ?', NOW() + INTERVAL '15 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Teste de eco GET na API REST local"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(8, NOW(), NOW(), 11, 'http://rest-api-unsecure:8081/api/echo?param1=value1&param2=value2', 'GET', '{"Content-Type": "application/json", "Accept": "application/json"}', NULL, 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);

-- REST API Unsecure - Echo POST
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(12, NOW(), NOW(), 'Echo POST API Local', 'Teste de eco POST na API REST local não segura', 'REST_CALL', 'ACTIVE', '0 */20 * * * ?', NOW() + INTERVAL '20 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Teste de eco POST na API REST local"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(9, NOW(), NOW(), 12, 'http://rest-api-unsecure:8081/api/echo', 'POST', '{"Content-Type": "application/json", "Accept": "application/json"}', '{"message": "Hello from scheduler", "timestamp": "2023-05-20T10:00:00", "source": "Task Scheduler"}', 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);

-- REST API Unsecure - Status Check
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(13, NOW(), NOW(), 'Verificar Status API Local', 'Verificação de status da API REST local não segura', 'REST_CALL', 'ACTIVE', '0 */3 * * * ?', NOW() + INTERVAL '3 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Verificação de status da API REST local"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(10, NOW(), NOW(), 13, 'http://rest-api-unsecure:8081/api/status', 'GET', '{"Content-Type": "application/json", "Accept": "application/json"}', NULL, 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);