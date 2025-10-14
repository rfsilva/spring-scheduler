@echo off
REM Script para importar dados de teste de mensageria para o task-scheduler

echo Importando dados de teste de mensageria para o task-scheduler...

REM Importar dados de teste de mensageria
mysql -h localhost -P 3306 -u root -proot taskscheduler < test_data_messaging.sql

echo Dados de teste de mensageria importados com sucesso!