-- Massa de dados de teste para validação de front-end e agendamentos
-- Inclui exemplos de endpoints públicos para REST e SOAP

-- Limpar dados existentes (se necessário)
DELETE FROM task_executions;
DELETE FROM rest_task_configs;
DELETE FROM soap_task_configs;
DELETE FROM low_platform_batch_configs;
DELETE FROM high_platform_batch_configs;
DELETE FROM messaging_task_configs;
DELETE FROM scheduled_tasks;

-- Inserir tarefas agendadas de exemplo

-- 1. REST API - Consulta de CEP (ViaCEP)
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(1, NOW(), NOW(), 'Consulta CEP ViaCEP', 'Consulta de CEP utilizando a API pública do ViaCEP', 'REST_CALL', 'ACTIVE', '0 0/30 * * * ?', NOW() + INTERVAL '30 MINUTE', NULL, NULL, NULL, 0, 3, 60, '{"description": "Consulta de CEP para validação de endereços"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(1, NOW(), NOW(), 1, 'https://viacep.com.br/ws/01001000/json/', 'GET', '{"Content-Type": "application/json"}', NULL, 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "EXPONENTIAL"}', NULL, NULL, NULL, NULL);

-- 2. REST API - Consulta de Feriados Nacionais
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(2, NOW(), NOW(), 'Consulta Feriados Nacionais', 'Consulta de feriados nacionais utilizando API pública', 'REST_CALL', 'ACTIVE', '0 0 1 * * ?', NOW() + INTERVAL '1 DAY', NULL, NULL, NULL, 0, 3, 60, '{"description": "Consulta de feriados nacionais para calendário corporativo"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(2, NOW(), NOW(), 2, 'https://brasilapi.com.br/api/feriados/v1/2023', 'GET', '{"Content-Type": "application/json"}', NULL, 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);

-- 3. REST API - Consulta de Cotação de Moedas
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(3, NOW(), NOW(), 'Cotação de Moedas', 'Consulta de cotação de moedas utilizando API pública', 'REST_CALL', 'ACTIVE', '0 0/15 9-18 ? * MON-FRI', NOW() + INTERVAL '15 MINUTE', NOW() - INTERVAL '15 MINUTE', 'SUCCESS', 'Cotação obtida com sucesso', 12, 3, 60, '{"description": "Consulta de cotação de moedas para sistema financeiro"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(3, NOW(), NOW(), 3, 'https://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL,GBP-BRL', 'GET', '{"Content-Type": "application/json"}', NULL, 30, 'NONE', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);

-- 4. REST API - Consulta de Clima
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(4, NOW(), NOW(), 'Consulta de Clima', 'Consulta de previsão do tempo utilizando API pública', 'REST_CALL', 'INACTIVE', '0 0 6,12,18 * * ?', NULL, NOW() - INTERVAL '6 HOUR', 'FAILED', 'Erro na conexão com o serviço', 45, 3, 60, '{"description": "Consulta de previsão do tempo para sistema de logística"}');

INSERT INTO rest_task_configs (id, created_at, updated_at, scheduled_task_id, url, method, headers, body, timeout, auth_type, retry_policy, token_endpoint, client_id, client_secret, certificate_path)
VALUES 
(4, NOW(), NOW(), 4, 'https://api.openweathermap.org/data/2.5/weather?q=Sao%20Paulo,br&appid=YOUR_API_KEY', 'GET', '{"Content-Type": "application/json"}', NULL, 30, 'API_KEY', '{"maxAttempts": 3, "backoffPolicy": "FIXED"}', NULL, NULL, NULL, NULL);

-- 5. SOAP - Consulta de Serviço de Calculadora
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(5, NOW(), NOW(), 'Calculadora SOAP', 'Teste de serviço SOAP de calculadora', 'SOAP_CALL', 'ACTIVE', '0 0/10 * * * ?', NOW() + INTERVAL '10 MINUTE', NOW() - INTERVAL '10 MINUTE', 'SUCCESS', 'Operação realizada com sucesso', 144, 3, 60, '{"description": "Teste de integração com serviço SOAP"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(1, NOW(), NOW(), 5, 'http://www.dneonline.com/calculator.asmx?WSDL', 'Add', 'http://tempuri.org/', 'http://tempuri.org/Add', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
   <soapenv:Header/>
   <soapenv:Body>
      <tem:Add>
         <tem:intA>10</tem:intA>
         <tem:intB>20</tem:intB>
      </tem:Add>
   </soapenv:Body>
</soapenv:Envelope>', 
'NONE', 30, '', '', '{}');

-- 6. SOAP - Consulta de Serviço de Conversão de Temperatura
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(6, NOW(), NOW(), 'Conversão de Temperatura', 'Serviço SOAP para conversão de temperatura', 'SOAP_CALL', 'ACTIVE', '0 0 */2 * * ?', NOW() + INTERVAL '2 HOUR', NOW() - INTERVAL '2 HOUR', 'SUCCESS', 'Conversão realizada com sucesso', 72, 3, 60, '{"description": "Conversão de temperatura para sistema de monitoramento"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(2, NOW(), NOW(), 6, 'https://www.w3schools.com/xml/tempconvert.asmx?WSDL', 'CelsiusToFahrenheit', 'https://www.w3schools.com/xml/', 'https://www.w3schools.com/xml/CelsiusToFahrenheit', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="https://www.w3schools.com/xml/">
   <soapenv:Header/>
   <soapenv:Body>
      <web:CelsiusToFahrenheit>
         <web:Celsius>25</web:Celsius>
      </web:CelsiusToFahrenheit>
   </soapenv:Body>
</soapenv:Envelope>', 
'NONE', 30, '', '', '{}');

-- 7. SOAP - Consulta de Serviço de Número por Extenso
INSERT INTO scheduled_tasks (id, created_at, updated_at, name, description, task_type, status, cron_expression, next_execution, last_execution, last_execution_status, last_execution_message, execution_count, max_retries, retry_delay_seconds, task_data)
VALUES 
(7, NOW(), NOW(), 'Número por Extenso', 'Serviço SOAP para converter número em texto por extenso', 'SOAP_CALL', 'INACTIVE', '0 30 9 * * ?', NULL, NOW() - INTERVAL '1 DAY', 'FAILED', 'Serviço indisponível', 30, 3, 60, '{"description": "Conversão de números para texto por extenso"}');

INSERT INTO soap_task_configs (id, created_at, updated_at, scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password, custom_headers)
VALUES 
(3, NOW(), NOW(), 7, 'https://www.dataaccess.com/webservicesserver/NumberConversion.wso?WSDL', 'NumberToWords', 'http://www.dataaccess.com/webservicesserver/', 'http://www.dataaccess.com/webservicesserver/NumberToWords', 
'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://www.dataaccess.com/webservicesserver/">
   <soapenv:Header/>
   <soapenv:Body>
      <web:NumberToWords>
         <web:ubiNum>1984</web:ubiNum>
      </web:NumberToWords>
   </soapenv:Body>
</soapenv:Envelope>', 
'NONE', 30, '', '', '{}');

-- Inserir execuções de tarefas para histórico

-- Execuções para a tarefa de Consulta CEP
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(1, NOW() - INTERVAL '2 DAY', NOW() - INTERVAL '2 DAY', 1, NOW() - INTERVAL '2 DAY', NOW() - INTERVAL '2 DAY', 'SUCCESS', '{"cep": "01001-000", "logradouro": "Praça da Sé", "complemento": "lado ímpar", "bairro": "Sé", "localidade": "São Paulo", "uf": "SP", "ibge": "3550308", "gia": "1004", "ddd": "11", "siafi": "7107"}', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(2, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 1, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 'SUCCESS', '{"cep": "01001-000", "logradouro": "Praça da Sé", "complemento": "lado ímpar", "bairro": "Sé", "localidade": "São Paulo", "uf": "SP", "ibge": "3550308", "gia": "1004", "ddd": "11", "siafi": "7107"}', NULL, 0);

-- Execuções para a tarefa de Feriados Nacionais
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(3, NOW() - INTERVAL '30 DAY', NOW() - INTERVAL '30 DAY', 2, NOW() - INTERVAL '30 DAY', NOW() - INTERVAL '30 DAY', 'SUCCESS', '[{"date":"2023-01-01","name":"Confraternização mundial","type":"national"},{"date":"2023-02-20","name":"Carnaval","type":"national"},{"date":"2023-02-21","name":"Carnaval","type":"national"},{"date":"2023-04-07","name":"Sexta-feira Santa","type":"national"},{"date":"2023-04-21","name":"Tiradentes","type":"national"},{"date":"2023-05-01","name":"Dia do trabalho","type":"national"},{"date":"2023-06-08","name":"Corpus Christi","type":"national"},{"date":"2023-09-07","name":"Independência do Brasil","type":"national"},{"date":"2023-10-12","name":"Nossa Senhora Aparecida","type":"national"},{"date":"2023-11-02","name":"Finados","type":"national"},{"date":"2023-11-15","name":"Proclamação da República","type":"national"},{"date":"2023-12-25","name":"Natal","type":"national"}]', NULL, 0);

-- Execuções para a tarefa de Cotação de Moedas
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(4, NOW() - INTERVAL '45 MINUTE', NOW() - INTERVAL '45 MINUTE', 3, NOW() - INTERVAL '45 MINUTE', NOW() - INTERVAL '45 MINUTE', 'SUCCESS', '{"USDBRL":{"code":"USD","codein":"BRL","name":"Dólar Americano/Real Brasileiro","high":"5.1746","low":"5.1276","varBid":"-0.0258","pctChange":"-0.5","bid":"5.1276","ask":"5.1282","timestamp":"1621440058","create_date":"2021-05-19 13:47:38"},"EURBRL":{"code":"EUR","codein":"BRL","name":"Euro/Real Brasileiro","high":"6.3108","low":"6.2669","varBid":"-0.0228","pctChange":"-0.36","bid":"6.2669","ask":"6.2699","timestamp":"1621440058","create_date":"2021-05-19 13:47:38"},"GBPBRL":{"code":"GBP","codein":"BRL","name":"Libra Esterlina/Real Brasileiro","high":"7.3230","low":"7.2690","varBid":"-0.0324","pctChange":"-0.44","bid":"7.2690","ask":"7.2740","timestamp":"1621440058","create_date":"2021-05-19 13:47:38"}}', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(5, NOW() - INTERVAL '30 MINUTE', NOW() - INTERVAL '30 MINUTE', 3, NOW() - INTERVAL '30 MINUTE', NOW() - INTERVAL '30 MINUTE', 'SUCCESS', '{"USDBRL":{"code":"USD","codein":"BRL","name":"Dólar Americano/Real Brasileiro","high":"5.1846","low":"5.1376","varBid":"0.0100","pctChange":"0.2","bid":"5.1376","ask":"5.1382","timestamp":"1621441858","create_date":"2021-05-19 14:17:38"},"EURBRL":{"code":"EUR","codein":"BRL","name":"Euro/Real Brasileiro","high":"6.3208","low":"6.2769","varBid":"0.0100","pctChange":"0.16","bid":"6.2769","ask":"6.2799","timestamp":"1621441858","create_date":"2021-05-19 14:17:38"},"GBPBRL":{"code":"GBP","codein":"BRL","name":"Libra Esterlina/Real Brasileiro","high":"7.3330","low":"7.2790","varBid":"0.0100","pctChange":"0.14","bid":"7.2790","ask":"7.2840","timestamp":"1621441858","create_date":"2021-05-19 14:17:38"}}', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(6, NOW() - INTERVAL '15 MINUTE', NOW() - INTERVAL '15 MINUTE', 3, NOW() - INTERVAL '15 MINUTE', NOW() - INTERVAL '15 MINUTE', 'SUCCESS', '{"USDBRL":{"code":"USD","codein":"BRL","name":"Dólar Americano/Real Brasileiro","high":"5.1946","low":"5.1476","varBid":"0.0200","pctChange":"0.4","bid":"5.1476","ask":"5.1482","timestamp":"1621443658","create_date":"2021-05-19 14:47:38"},"EURBRL":{"code":"EUR","codein":"BRL","name":"Euro/Real Brasileiro","high":"6.3308","low":"6.2869","varBid":"0.0200","pctChange":"0.32","bid":"6.2869","ask":"6.2899","timestamp":"1621443658","create_date":"2021-05-19 14:47:38"},"GBPBRL":{"code":"GBP","codein":"BRL","name":"Libra Esterlina/Real Brasileiro","high":"7.3430","low":"7.2890","varBid":"0.0200","pctChange":"0.28","bid":"7.2890","ask":"7.2940","timestamp":"1621443658","create_date":"2021-05-19 14:47:38"}}', NULL, 0);

-- Execuções para a tarefa de Consulta de Clima
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(7, NOW() - INTERVAL '12 HOUR', NOW() - INTERVAL '12 HOUR', 4, NOW() - INTERVAL '12 HOUR', NOW() - INTERVAL '12 HOUR', 'FAILED', NULL, 'Erro na autenticação: API key inválida', 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(8, NOW() - INTERVAL '12 HOUR', NOW() - INTERVAL '12 HOUR', 4, NOW() - INTERVAL '12 HOUR', NOW() - INTERVAL '12 HOUR', 'FAILED', NULL, 'Erro na autenticação: API key inválida', 1);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(9, NOW() - INTERVAL '12 HOUR', NOW() - INTERVAL '12 HOUR', 4, NOW() - INTERVAL '12 HOUR', NOW() - INTERVAL '12 HOUR', 'FAILED', NULL, 'Erro na autenticação: API key inválida', 2);

-- Execuções para a tarefa de Calculadora SOAP
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(10, NOW() - INTERVAL '30 MINUTE', NOW() - INTERVAL '30 MINUTE', 5, NOW() - INTERVAL '30 MINUTE', NOW() - INTERVAL '30 MINUTE', 'SUCCESS', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><AddResponse xmlns="http://tempuri.org/"><AddResult>30</AddResult></AddResponse></soap:Body></soap:Envelope>', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(11, NOW() - INTERVAL '20 MINUTE', NOW() - INTERVAL '20 MINUTE', 5, NOW() - INTERVAL '20 MINUTE', NOW() - INTERVAL '20 MINUTE', 'SUCCESS', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><AddResponse xmlns="http://tempuri.org/"><AddResult>30</AddResult></AddResponse></soap:Body></soap:Envelope>', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(12, NOW() - INTERVAL '10 MINUTE', NOW() - INTERVAL '10 MINUTE', 5, NOW() - INTERVAL '10 MINUTE', NOW() - INTERVAL '10 MINUTE', 'SUCCESS', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><AddResponse xmlns="http://tempuri.org/"><AddResult>30</AddResult></AddResponse></soap:Body></soap:Envelope>', NULL, 0);

-- Execuções para a tarefa de Conversão de Temperatura
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(13, NOW() - INTERVAL '6 HOUR', NOW() - INTERVAL '6 HOUR', 6, NOW() - INTERVAL '6 HOUR', NOW() - INTERVAL '6 HOUR', 'SUCCESS', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><CelsiusToFahrenheitResponse xmlns="https://www.w3schools.com/xml/"><CelsiusToFahrenheitResult>77</CelsiusToFahrenheitResult></CelsiusToFahrenheitResponse></soap:Body></soap:Envelope>', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(14, NOW() - INTERVAL '4 HOUR', NOW() - INTERVAL '4 HOUR', 6, NOW() - INTERVAL '4 HOUR', NOW() - INTERVAL '4 HOUR', 'SUCCESS', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><CelsiusToFahrenheitResponse xmlns="https://www.w3schools.com/xml/"><CelsiusToFahrenheitResult>77</CelsiusToFahrenheitResult></CelsiusToFahrenheitResponse></soap:Body></soap:Envelope>', NULL, 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(15, NOW() - INTERVAL '2 HOUR', NOW() - INTERVAL '2 HOUR', 6, NOW() - INTERVAL '2 HOUR', NOW() - INTERVAL '2 HOUR', 'SUCCESS', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><CelsiusToFahrenheitResponse xmlns="https://www.w3schools.com/xml/"><CelsiusToFahrenheitResult>77</CelsiusToFahrenheitResult></CelsiusToFahrenheitResponse></soap:Body></soap:Envelope>', NULL, 0);

-- Execuções para a tarefa de Número por Extenso
INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(16, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 7, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 'FAILED', NULL, 'Erro de conexão: Timeout ao conectar com o serviço', 0);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(17, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 7, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 'FAILED', NULL, 'Erro de conexão: Timeout ao conectar com o serviço', 1);

INSERT INTO task_executions (id, created_at, updated_at, scheduled_task_id, start_time, end_time, status, result, error_message, retry_count)
VALUES 
(18, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 7, NOW() - INTERVAL '1 DAY', NOW() - INTERVAL '1 DAY', 'FAILED', NULL, 'Erro de conexão: Timeout ao conectar com o serviço', 2);