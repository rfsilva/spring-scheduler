@echo off
REM Script para testar os serviços de mensageria (Kafka e RabbitMQ)

echo Testando serviços de mensageria...

REM Verificar se o Docker está em execução
docker info > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
  echo Erro: Docker não está em execução. Por favor, inicie o Docker e tente novamente.
  exit /b 1
)

REM Verificar se os serviços estão em execução
docker ps | findstr "taskscheduler-kafka" > nul
if %ERRORLEVEL% NEQ 0 (
  echo Erro: Serviço Kafka não está em execução. Execute 'docker-compose up -d' primeiro.
  exit /b 1
)

docker ps | findstr "taskscheduler-rabbitmq" > nul
if %ERRORLEVEL% NEQ 0 (
  echo Erro: Serviço RabbitMQ não está em execução. Execute 'docker-compose up -d' primeiro.
  exit /b 1
)

echo === Testando Kafka ===

REM Enviar mensagem para o tópico task-notifications
echo Enviando mensagem para o tópico task-notifications...
echo {"notification_id": "TEST-%TIME%", "timestamp": "%DATE% %TIME%", "level": "INFO", "message": "Mensagem de teste via script", "source": "test-script"} > kafka_test_message.txt
docker exec taskscheduler-kafka kafka-console-producer --bootstrap-server kafka:9092 --topic task-notifications < kafka_test_message.txt
del kafka_test_message.txt

REM Verificar se a mensagem foi enviada
echo Verificando as últimas mensagens do tópico task-notifications...
docker exec taskscheduler-kafka kafka-console-consumer --bootstrap-server kafka:9092 --topic task-notifications --from-beginning --max-messages 1

echo.
echo === Testando RabbitMQ ===

REM Enviar mensagem para a fila task-queue
echo Enviando mensagem para a fila task-queue...
docker exec taskscheduler-rabbitmq rabbitmqadmin publish exchange=task-exchange routing_key=task-routing-key payload="{\"task_id\": \"TEST-%TIME%\", \"timestamp\": \"%DATE% %TIME%\", \"action\": \"TEST\", \"data\": {\"source\": \"test-script\"}}"

REM Verificar se a mensagem foi enviada
echo Verificando se a mensagem foi entregue à fila task-queue...
docker exec taskscheduler-rabbitmq rabbitmqadmin get queue=task-queue count=1 requeue=true

echo.
echo Testes concluídos!
echo Para mais detalhes, acesse:
echo - Kafka UI: http://localhost:8090
echo - RabbitMQ Management: http://localhost:15672 (usuário: admin, senha: admin123)