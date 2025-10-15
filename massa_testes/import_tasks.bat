@echo off
setlocal enabledelayedexpansion

REM Script para importar todas as tarefas de teste para o Task Scheduler

REM URL base da API do Task Scheduler
set API_URL=http://localhost:8080/api/tasks

echo Iniciando importacao de tarefas de teste...

REM Contador de sucesso e falha
set success_count=0
set fail_count=0

REM Importar cada arquivo JSON
for %%f in (*.json) do (
    echo.
    echo Importando %%f...
    
    REM Enviar requisicao POST para a API
    curl -s -o response.tmp -w "%%{http_code}" -X POST ^
      -H "Content-Type: application/json" ^
      -d @"%%f" ^
      %API_URL% > http_code.tmp
    
    REM Ler o código de status HTTP
    set /p http_code=<http_code.tmp
    
    REM Verificar se a requisicao foi bem-sucedida
    if !http_code! GEQ 200 if !http_code! LSS 300 (
        echo [32m✓ Tarefa importada com sucesso: %%f[0m
        set /a success_count+=1
    ) else (
        echo [31m✗ Falha ao importar tarefa: %%f[0m
        echo [31mCodigo HTTP: !http_code![0m
        echo [31mResposta:[0m
        type response.tmp
        set /a fail_count+=1
    )
)

REM Limpar arquivos temporários
del response.tmp 2>nul
del http_code.tmp 2>nul

echo.
echo Resumo da importacao:
echo [32mTarefas importadas com sucesso: %success_count%[0m
echo [31mTarefas com falha na importacao: %fail_count%[0m

if %fail_count% EQU 0 (
    echo.
    echo [32mTodas as tarefas foram importadas com sucesso![0m
) else (
    echo.
    echo [33mAlgumas tarefas nao puderam ser importadas. Verifique os erros acima.[0m
)

endlocal