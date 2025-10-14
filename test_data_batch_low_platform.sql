-- Adicionar tarefas agendadas para o batch-low-platform usando o tipo LOW_PLATFORM_BATCH

-- Batch Low Platform - Processamento Diário
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(11, NOW(), NOW(), 'Processamento Batch Diário', 'Execução diária do processamento em lote na plataforma baixa', 'LOW_PLATFORM_BATCH', 'ACTIVE', '0 0 2 * * ?', NOW() + INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 'SUCCESS', 'Processamento concluído com sucesso', 30, 3, 60, '{"description": "Processamento em lote diário para dados financeiros"}');

INSERT INTO low_platform_batch_configs (id, created_at, updated_at, scheduled_task_id, server_url, job_name, parameters, timeout)
VALUES 
(1, NOW(), NOW(), 11, 'http://batch-low-platform:8083', 'processJob', '{"time": "' || EXTRACT(EPOCH FROM NOW()) * 1000 || '"}', 3600);

-- Batch Low Platform - Processamento Semanal
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(12, NOW(), NOW(), 'Processamento Batch Semanal', 'Execução semanal do processamento em lote na plataforma baixa', 'LOW_PLATFORM_BATCH', 'ACTIVE', '0 0 3 ? * SUN', NOW() + INTERVAL '7 DAY', NOW() - INTERVAL '7 DAY', 'SUCCESS', 'Processamento concluído com sucesso', 4, 3, 60, '{"description": "Processamento em lote semanal para consolidação de dados"}');

INSERT INTO low_platform_batch_configs (id, created_at, updated_at, scheduled_task_id, server_url, job_name, parameters, timeout)
VALUES 
(2, NOW(), NOW(), 12, 'http://batch-low-platform:8083', 'processJob', '{"time": "' || EXTRACT(EPOCH FROM NOW()) * 1000 || '", "type": "WEEKLY"}', 7200);

-- Batch Low Platform - Processamento Mensal
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(13, NOW(), NOW(), 'Processamento Batch Mensal', 'Execução mensal do processamento em lote na plataforma baixa', 'LOW_PLATFORM_BATCH', 'ACTIVE', '0 0 4 1 * ?', NOW() + INTERVAL '30 DAY', NOW() - INTERVAL '30 DAY', 'SUCCESS', 'Processamento concluído com sucesso', 12, 3, 60, '{"description": "Processamento em lote mensal para fechamento contábil"}');

INSERT INTO low_platform_batch_configs (id, created_at, updated_at, scheduled_task_id, server_url, job_name, parameters, timeout)
VALUES 
(3, NOW(), NOW(), 13, 'http://batch-low-platform:8083', 'processJob', '{"time": "' || EXTRACT(EPOCH FROM NOW()) * 1000 || '", "type": "MONTHLY"}', 14400);

-- Execuções para a tarefa de Processamento Batch Diário
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(22, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 11, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 'SUCCESS', '{"jobId":"123456","status":"COMPLETED","processedItems":10,"failedItems":0,"executionTime":45.2}', NULL, 0);

-- Execuções para a tarefa de Processamento Batch Semanal
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(23, NOW() - INTERVAL '7 DAY', NOW() - INTERVAL '7 DAY', 12, NOW() - INTERVAL '7 DAY', NOW() - INTERVAL '7 DAY', 'SUCCESS', '{"jobId":"123457","status":"COMPLETED","processedItems":50,"failedItems":2,"executionTime":325.7}', NULL, 0);

-- Execuções para a tarefa de Processamento Batch Mensal
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(24, NOW() - INTERVAL '30 DAY', NOW() - INTERVAL '30 DAY', 13, NOW() - INTERVAL '30 DAY', NOW() - INTERVAL '30 DAY', 'SUCCESS', '{"jobId":"123458","status":"COMPLETED","processedItems":200,"failedItems":5,"executionTime":1250.3}', NULL, 0);