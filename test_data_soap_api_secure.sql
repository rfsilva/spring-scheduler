-- Adicionar tarefas SOAP para o serviço soap-api-secure

-- 1. SOAP Secure - Operação de Adição com autenticação básica
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(14, NOW(), NOW(), 'Calculadora SOAP Segura - Adição', 'Teste de adição usando o serviço SOAP seguro', 'SOAP_CALL', 'ACTIVE', '0 */5 * * * ?', NOW() + INTERVAL '5 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Teste de integração com serviço SOAP seguro"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(7, NOW(), NOW(), 14, 'http://soap-api-secure:8085/ws/calculator.wsdl', 'add', 'http://example.com/soap/calculator/secure', 'http://example.com/soap/calculator/secure/add', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator/secure">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:addRequest>
         <cal:a>50</cal:a>
         <cal:b>25</cal:b>
      </cal:addRequest>
   </soapenv:Body>
</soapenv:Envelope>', 
'BASIC', 30, 'admin', 'password123', '{}');

-- 2. SOAP Secure - Operação de Multiplicação com autenticação básica
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(15, NOW(), NOW(), 'Calculadora SOAP Segura - Multiplicação', 'Teste de multiplicação usando o serviço SOAP seguro', 'SOAP_CALL', 'ACTIVE', '0 */10 * * * ?', NOW() + INTERVAL '10 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Teste de integração com serviço SOAP seguro"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(8, NOW(), NOW(), 15, 'http://soap-api-secure:8085/ws/calculator.wsdl', 'multiply', 'http://example.com/soap/calculator/secure', 'http://example.com/soap/calculator/secure/multiply', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator/secure">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:multiplyRequest>
         <cal:a>12</cal:a>
         <cal:b>8</cal:b>
      </cal:multiplyRequest>
   </soapenv:Body>
</soapenv:Envelope>', 
'BASIC', 30, 'admin', 'password123', '{}');

-- 3. SOAP Secure - Verificação de Status com autenticação básica
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(16, NOW(), NOW(), 'Verificar Status SOAP Seguro', 'Verificação de status do serviço SOAP seguro', 'SOAP_CALL', 'ACTIVE', '0 */15 * * * ?', NOW() + INTERVAL '15 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Monitoramento do serviço SOAP seguro"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(9, NOW(), NOW(), 16, 'http://soap-api-secure:8085/ws/calculator.wsdl', 'status', 'http://example.com/soap/calculator/secure', 'http://example.com/soap/calculator/secure/status', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator/secure">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:statusRequest/>
   </soapenv:Body>
</soapenv:Envelope>', 
'BASIC', 30, 'admin', 'password123', '{}');

-- 4. SOAP Secure - Operação de Divisão com autenticação básica (credenciais inválidas para testar falha)
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(17, NOW(), NOW(), 'Calculadora SOAP Segura - Divisão (Falha)', 'Teste de divisão usando o serviço SOAP seguro com credenciais inválidas', 'SOAP_CALL', 'ACTIVE', '0 */20 * * * ?', NOW() + INTERVAL '20 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Teste de falha de autenticação com serviço SOAP seguro"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(10, NOW(), NOW(), 17, 'http://soap-api-secure:8085/ws/calculator.wsdl', 'divide', 'http://example.com/soap/calculator/secure', 'http://example.com/soap/calculator/secure/divide', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator/secure">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:divideRequest>
         <cal:a>100</cal:a>
         <cal:b>4</cal:b>
      </cal:divideRequest>
   </soapenv:Body>
</soapenv:Envelope>', 
'BASIC', 30, 'admin', 'senha_errada', '{}');