# Massa de Testes para Task Scheduler

Este diretório contém uma coleção de arquivos JSON que representam diferentes tipos de tarefas para testar o Task Scheduler. Todos os casos de teste estão configurados inicialmente com status `INACTIVE`.

## Casos de Teste

### APIs REST

1. **01_rest_api_public_open.json**
   - API pública do Open Weather Map (sem autenticação)
   - Obtém dados meteorológicos de São Paulo

2. **02_rest_api_public_auth.json**
   - API pública da NASA com chave de API para demonstração
   - Usa a chave de API "DEMO_KEY" fornecida pela NASA

3. **03_rest_api_unsecure.json**
   - Chamada à nossa API REST não segura (rest-api-unsecure)
   - Obtém a lista de tarefas

4. **04_rest_api_secure_correct_auth.json**
   - Chamada à nossa API REST segura (rest-api-secure) com credenciais corretas
   - Usa as credenciais: admin/admin123

5. **05_rest_api_secure_incorrect_auth.json**
   - Chamada à nossa API REST segura (rest-api-secure) com credenciais incorretas
   - Usa credenciais inválidas para testar o comportamento de erro

### APIs SOAP

6. **06_soap_api_public_open.json**
   - Serviço SOAP público de conversão de temperatura
   - Converte 25°C para Fahrenheit

7. **07_soap_api_public_auth.json**
   - Serviço SOAP público com autenticação básica
   - Lista nomes de países por código

8. **08_soap_api_unsecure.json**
   - Chamada à nossa API SOAP não segura (soap-api-unsecure)
   - Realiza uma operação de adição (10 + 20)

9. **09_soap_api_secure_correct_auth.json**
   - Chamada à nossa API SOAP segura (soap-api-secure) com credenciais corretas
   - Usa as credenciais: admin/admin123

10. **10_soap_api_secure_incorrect_auth.json**
    - Chamada à nossa API SOAP segura (soap-api-secure) com credenciais incorretas
    - Usa credenciais inválidas para testar o comportamento de erro

### Batch

11. **11_batch_low_platform.json**
    - Execução do nosso serviço batch de baixa plataforma
    - Configura variáveis de ambiente e comandos de sucesso/falha

### Mensageria

12. **12_kafka_messaging.json**
    - Envio de mensagem para um tópico Kafka (task-events)
    - Usa as configurações do Kafka definidas no docker-compose

13. **13_rabbitmq_messaging.json**
    - Envio de mensagem para uma fila RabbitMQ (task-queue)
    - Usa as credenciais: admin/admin123 definidas no docker-compose

## Como Usar

Para importar esses casos de teste:

1. Acesse a interface do Task Scheduler
2. Vá para a seção de criação de tarefas
3. Importe cada arquivo JSON individualmente ou use a API para importação em lote
4. Após importar, você pode ativar as tarefas conforme necessário para testá-las

## Observações

- Todos os casos de teste estão configurados com status `INACTIVE` para evitar execuções automáticas
- As expressões cron estão configuradas para execuções periódicas (a cada 10-45 minutos ou em horários específicos)
- As credenciais usadas são apenas para fins de teste e devem ser alteradas em ambientes de produção