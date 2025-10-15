#!/bin/bash

# Script para importar todas as tarefas de teste para o Task Scheduler

# URL base da API do Task Scheduler
API_URL="http://localhost:8080/api/tasks"

# Cores para saída
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Iniciando importação de tarefas de teste...${NC}"

# Contador de sucesso e falha
success_count=0
fail_count=0

# Importar cada arquivo JSON
for file in *.json; do
  if [ -f "$file" ] && [ "$file" != "README.md" ]; then
    echo -e "\nImportando $file..."
    
    # Enviar requisição POST para a API
    response=$(curl -s -w "%{http_code}" -X POST \
      -H "Content-Type: application/json" \
      -d @"$file" \
      $API_URL)
    
    # Extrair o código de status HTTP
    http_code=${response: -3}
    response_body=${response:0:${#response}-3}
    
    # Verificar se a requisição foi bem-sucedida
    if [[ $http_code -ge 200 && $http_code -lt 300 ]]; then
      echo -e "${GREEN}✓ Tarefa importada com sucesso: $file${NC}"
      ((success_count++))
    else
      echo -e "${RED}✗ Falha ao importar tarefa: $file${NC}"
      echo -e "${RED}Código HTTP: $http_code${NC}"
      echo -e "${RED}Resposta: $response_body${NC}"
      ((fail_count++))
    fi
  fi
done

echo -e "\n${YELLOW}Resumo da importação:${NC}"
echo -e "${GREEN}Tarefas importadas com sucesso: $success_count${NC}"
echo -e "${RED}Tarefas com falha na importação: $fail_count${NC}"

if [ $fail_count -eq 0 ]; then
  echo -e "\n${GREEN}Todas as tarefas foram importadas com sucesso!${NC}"
else
  echo -e "\n${YELLOW}Algumas tarefas não puderam ser importadas. Verifique os erros acima.${NC}"
fi