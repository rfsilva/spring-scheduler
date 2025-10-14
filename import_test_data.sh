#!/bin/bash

# Script para importar dados de teste para o Task Scheduler

echo "Importando dados de teste para o Task Scheduler..."

# Verificar se o Docker está em execução
if [ "$(docker ps -q -f name=task-scheduler-db)" ]; then
    echo "Importando dados para o container Docker..."
    
    # Copiar o arquivo SQL para o container
    docker cp test_data.sql task-scheduler-db:/tmp/
    
    # Executar o script SQL no banco de dados
    docker exec -it task-scheduler-db psql -U postgres -d taskscheduler -f /tmp/test_data.sql
    
    echo "Dados importados com sucesso para o container Docker!"
else
    # Tentar importar localmente
    echo "Container Docker não encontrado. Tentando importar localmente..."
    
    # Solicitar informações de conexão
    read -p "Digite o nome do banco de dados [taskscheduler]: " dbname
    dbname=${dbname:-taskscheduler}
    
    read -p "Digite o nome de usuário do banco de dados [postgres]: " dbuser
    dbuser=${dbuser:-postgres}
    
    read -s -p "Digite a senha do banco de dados: " dbpassword
    echo ""
    
    # Exportar senha como variável de ambiente
    export PGPASSWORD="$dbpassword"
    
    # Executar o script SQL
    psql -U "$dbuser" -d "$dbname" -f test_data.sql
    
    # Limpar a senha da variável de ambiente
    unset PGPASSWORD
    
    echo "Dados importados com sucesso localmente!"
fi

echo ""
echo "Resumo dos dados importados:"
echo "- 7 tarefas agendadas (4 REST e 3 SOAP)"
echo "- 18 execuções de tarefas para histórico"
echo "- API REST não segura usa banco de dados H2 em memória com dados pré-carregados"
echo "- API REST segura usa banco de dados H2 em memória com dados pré-carregados"
echo ""
echo "Para testar os endpoints, consulte o arquivo endpoint_testing_examples.md"
echo "Para mais informações, consulte o arquivo test_data_README.md"