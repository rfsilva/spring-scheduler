# Exemplos de Expressões Cron para Testes

Este documento contém exemplos de expressões cron que podem ser usadas para testar a execução imediata ou em intervalos curtos das tarefas agendadas.

## Formato da Expressão Cron

O formato padrão de uma expressão cron é:

```
┌───────────── segundo (0-59)
│ ┌───────────── minuto (0-59)
│ │ ┌───────────── hora (0-23)
│ │ │ ┌───────────── dia do mês (1-31)
│ │ │ │ ┌───────────── mês (1-12)
│ │ │ │ │ ┌───────────── dia da semana (0-6) (Domingo=0)
│ │ │ │ │ │
│ │ │ │ │ │
* * * * * *
```

## Expressões para Execução Imediata

Para testar a execução imediata de uma tarefa, você pode usar as seguintes expressões:

### Executar a cada minuto

```
0 * * * * ?
```

### Executar a cada 10 segundos

```
*/10 * * * * ?
```

### Executar a cada 30 segundos

```
0,30 * * * * ?
```

## Como Atualizar as Tarefas para Teste

Para atualizar as expressões cron das tarefas existentes, execute o seguinte SQL:

```sql
-- Atualizar todas as tarefas para execução a cada minuto
UPDATE scheduled_tasks SET cron_expression = '0 * * * * ?';

-- Atualizar uma tarefa específica para execução a cada 30 segundos
UPDATE scheduled_tasks SET cron_expression = '0,30 * * * * ?' WHERE id = 1;

-- Atualizar uma tarefa específica para execução a cada 10 segundos
UPDATE scheduled_tasks SET cron_expression = '*/10 * * * * ?' WHERE id = 2;
```

## Expressões Cron Úteis para Testes

| Descrição | Expressão Cron |
|-----------|---------------|
| A cada minuto | `0 * * * * ?` |
| A cada 5 minutos | `0 */5 * * * ?` |
| A cada 30 segundos | `0,30 * * * * ?` |
| A cada 10 segundos | `*/10 * * * * ?` |
| A cada 2 minutos | `0 */2 * * * ?` |
| No próximo minuto | `0 MINUTO+1 * * * ?` (substitua MINUTO+1 pelo próximo minuto) |

## Verificando a Próxima Execução

Para verificar quando será a próxima execução de uma tarefa, você pode consultar:

```sql
SELECT id, name, cron_expression, next_execution 
FROM scheduled_tasks 
ORDER BY next_execution;
```

## Ativando Tarefas Inativas

Para ativar tarefas que estão inativas:

```sql
UPDATE scheduled_tasks SET status = 'ACTIVE' WHERE status = 'INACTIVE';
```

## Verificando Execuções Recentes

Para verificar as execuções mais recentes:

```sql
SELECT te.id, st.name, te.start_time, te.end_time, te.status, te.error_message
FROM task_executions te
JOIN scheduled_tasks st ON te.scheduled_task_id = st.id
ORDER BY te.start_time DESC
LIMIT 10;
```