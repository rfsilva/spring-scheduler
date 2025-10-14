@echo off
REM Script para importar dados de teste para o Task Scheduler no Windows

echo Importando dados de teste para o Task Scheduler...

REM Verificar se o Docker está em execução
docker ps -q -f name=task-scheduler-db > nul 2>&1
if %ERRORLEVEL% == 0 (
    echo Importando dados para o container Docker...
    
    REM Copiar o arquivo SQL para o container
    docker cp test_data.sql task-scheduler-db:/tmp/
    
    REM Executar o script SQL no banco de dados
    docker exec -it task-scheduler-db psql -U postgres -d taskscheduler -f /tmp/test_data.sql
    
    echo Dados importados com sucesso para o container Docker!
) else (
    REM Tentar importar localmente
    echo Container Docker nao encontrado. Tentando importar localmente...
    
    REM Solicitar informações de conexão
    set /p dbname=Digite o nome do banco de dados [taskscheduler]: 
    if "%dbname%"=="" set dbname=taskscheduler
    
    set /p dbuser=Digite o nome de usuario do banco de dados [postgres]: 
    if "%dbuser%"=="" set dbuser=postgres
    
    set /p dbpassword=Digite a senha do banco de dados: 
    
    REM Executar o script SQL
    set PGPASSWORD=%dbpassword%
    psql -U %dbuser% -d %dbname% -f test_data.sql
    
    REM Limpar a senha da variável de ambiente
    set PGPASSWORD=
    
    echo Dados importados com sucesso localmente!
)

echo.
echo Resumo dos dados importados:
echo - 7 tarefas agendadas (4 REST e 3 SOAP)
echo - 18 execucoes de tarefas para historico
echo - API REST nao segura usa banco de dados H2 em memoria com dados pre-carregados
echo - API REST segura usa banco de dados H2 em memoria com dados pre-carregados
echo.
echo Para testar os endpoints, consulte o arquivo endpoint_testing_examples.md
echo Para mais informacoes, consulte o arquivo test_data_README.md

pause