#!/bin/bash

# Script para importar dados de teste de mensageria para o task-scheduler

echo "Importando dados de teste de mensageria para o task-scheduler..."

# Esperar o MySQL estar pronto
echo "Aguardando MySQL estar disponível..."
until mysql -h localhost -P 3306 -u root -proot -e "SELECT 1" >/dev/null 2>&1; do
  echo "MySQL ainda não está disponível, aguardando..."
  sleep 5
done

echo "MySQL disponível, importando dados..."

# Importar dados de teste de mensageria
mysql -h localhost -P 3306 -u root -proot taskscheduler < test_data_messaging.sql

echo "Dados de teste de mensageria importados com sucesso!"