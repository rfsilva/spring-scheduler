-- Adicionar tarefas SOAP para o serviço soap-api-unsecure

-- 1. SOAP - Operação de Adição
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(11, NOW(), NOW(), 'Calculadora SOAP - Adição', 'Teste de adição usando o serviço SOAP local', 'SOAP_CALL', 'ACTIVE', '0 */5 * * * ?', NOW() + INTERVAL '5 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Teste de integração com serviço SOAP local"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(4, NOW(), NOW(), 11, 'http://soap-api-unsecure:8084/ws/calculator.wsdl', 'add', 'http://example.com/soap/calculator', 'http://example.com/soap/calculator/add', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:addRequest>
         <cal:a>25</cal:a>
         <cal:b>30</cal:b>
      </cal:addRequest>
   </soapenv:Body>
</soapenv:Envelope>', 
'NONE', 30, '', '', '{}');

-- 2. SOAP - Operação de Multiplicação
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(12, NOW(), NOW(), 'Calculadora SOAP - Multiplicação', 'Teste de multiplicação usando o serviço SOAP local', 'SOAP_CALL', 'ACTIVE', '0 */10 * * * ?', NOW() + INTERVAL '10 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Teste de integração com serviço SOAP local"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(5, NOW(), NOW(), 12, 'http://soap-api-unsecure:8084/ws/calculator.wsdl', 'multiply', 'http://example.com/soap/calculator', 'http://example.com/soap/calculator/multiply', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:multiplyRequest>
         <cal:a>10</cal:a>
         <cal:b>5</cal:b>
      </cal:multiplyRequest>
   </soapenv:Body>
</soapenv:Envelope>', 
'NONE', 30, '', '', '{}');

-- 3. SOAP - Verificação de Status
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(13, NOW(), NOW(), 'Verificar Status SOAP', 'Verificação de status do serviço SOAP local', 'SOAP_CALL', 'ACTIVE', '0 */15 * * * ?', NOW() + INTERVAL '15 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Monitoramento do serviço SOAP local"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(6, NOW(), NOW(), 13, 'http://soap-api-unsecure:8084/ws/calculator.wsdl', 'status', 'http://example.com/soap/calculator', 'http://example.com/soap/calculator/status', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:statusRequest/>
   </soapenv:Body>
</soapenv:Envelope>', 
'NONE', 30, '', '', '{}');