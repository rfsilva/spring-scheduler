#!/bin/bash

# Script para testar os serviços de mensageria (Kafka e RabbitMQ)

echo "Testando serviços de mensageria..."

# Verificar se o Docker está em execução
if ! docker info > /dev/null 2>&1; then
  echo "Erro: Docker não está em execução. Por favor, inicie o Docker e tente novamente."
  exit 1
fi

# Verificar se os serviços estão em execução
if ! docker ps | grep -q "taskscheduler-kafka"; then
  echo "Erro: Serviço Kafka não está em execução. Execute 'docker-compose up -d' primeiro."
  exit 1
fi

if ! docker ps | grep -q "taskscheduler-rabbitmq"; then
  echo "Erro: Serviço RabbitMQ não está em execução. Execute 'docker-compose up -d' primeiro."
  exit 1
fi

echo "=== Testando Kafka ==="

# Enviar mensagem para o tópico task-notifications
echo "Enviando mensagem para o tópico task-notifications..."
docker exec taskscheduler-kafka kafka-console-producer --bootstrap-server kafka:9092 --topic task-notifications << EOF
{"notification_id": "TEST-$(date +%s)", "timestamp": "$(date -Iseconds)", "level": "INFO", "message": "Mensagem de teste via script", "source": "test-script"}
EOF

# Verificar se a mensagem foi enviada
echo "Verificando as últimas mensagens do tópico task-notifications..."
docker exec taskscheduler-kafka kafka-console-consumer --bootstrap-server kafka:9092 --topic task-notifications --from-beginning --max-messages 1

echo ""
echo "=== Testando RabbitMQ ==="

# Enviar mensagem para a fila task-queue
echo "Enviando mensagem para a fila task-queue..."
docker exec taskscheduler-rabbitmq rabbitmqadmin publish exchange=task-exchange routing_key=task-routing-key payload="{\"task_id\": \"TEST-$(date +%s)\", \"timestamp\": \"$(date -Iseconds)\", \"action\": \"TEST\", \"data\": {\"source\": \"test-script\"}}"

# Verificar se a mensagem foi enviada
echo "Verificando se a mensagem foi entregue à fila task-queue..."
docker exec taskscheduler-rabbitmq rabbitmqadmin get queue=task-queue count=1 requeue=true

echo ""
echo "Testes concluídos!"
echo "Para mais detalhes, acesse:"
echo "- Kafka UI: http://localhost:8090"
echo "- RabbitMQ Management: http://localhost:15672 (usuário: admin, senha: admin123)"