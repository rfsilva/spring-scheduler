# Análise de Compatibilidade: Task-Scheduler e Batch-Low-Platform

Este documento analisa a compatibilidade entre o task-scheduler e o batch-low-platform no contexto específico de execução de batch jobs via prompt de comando.

## Resumo Executivo

Existe uma **incompatibilidade fundamental** entre a implementação atual do task-scheduler para execução de batch em plataforma baixa e a implementação do batch-low-platform. O task-scheduler está projetado para executar comandos diretamente no sistema operacional, enquanto o batch-low-platform está implementado como uma aplicação Spring Batch com interface REST.

## 1. Análise do Task-Scheduler para Execução de Batch

### Implementação Atual

O `LowPlatformBatchExecutor` do task-scheduler está projetado para:

1. Executar comandos diretamente no sistema operacional usando `ProcessBuilder`
2. Passar parâmetros via linha de comando
3. Definir diretório de trabalho para o processo
4. Opcionalmente executar como um usuário específico
5. Capturar saída padrão e saída de erro
6. Executar comandos adicionais em caso de sucesso ou falha

Trecho relevante do código:

```java
private String executeBatchCommand(LowPlatformBatchConfig config) throws Exception {
    List<String> commandParts = new ArrayList<>();
    
    // Add command
    commandParts.add(config.getCommand());
    
    // Add parameters if present
    if (config.getParameters() != null && !config.getParameters().isEmpty()) {
        String[] params = config.getParameters().split("\\s+");
        for (String param : params) {
            commandParts.add(param);
        }
    }
    
    // Create process builder
    ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
    processBuilder.directory(new File(config.getWorkingDirectory()));
    
    // Start process
    Process process = processBuilder.start();
    // ...
}
```

### Modelo de Configuração

O modelo `LowPlatformBatchConfig` está projetado para armazenar:

1. Nome do job (`jobName`)
2. Comando a ser executado (`command`)
3. Parâmetros para o comando (`parameters`)
4. Diretório de trabalho (`workingDirectory`)
5. Timeout para execução (`timeout`)
6. Usuário para execução (`runAsUser`)
7. Comandos para execução em caso de sucesso ou falha (`onSuccess`, `onFailure`)

## 2. Análise do Batch-Low-Platform

### Implementação Atual

O batch-low-platform está implementado como:

1. Uma aplicação Spring Boot com Spring Batch
2. Jobs e steps definidos como beans Spring
3. Interface REST para iniciar jobs (`/api/batch/start`)
4. Interface REST para verificar status (`/api/batch/status`)
5. Sem interface de linha de comando direta

Trecho relevante do código:

```java
@PostMapping("/start")
public ResponseEntity<Map<String, Object>> startBatch() {
    Map<String, Object> response = new HashMap<>();
    
    try {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        
        jobLauncher.run(processJob, jobParameters);
        // ...
    }
    // ...
}
```

### Arquitetura

O batch-low-platform é uma aplicação Spring Batch completa:

1. Configurada com `@Configuration` e beans Spring
2. Executada como um serviço web
3. Containerizada via Docker
4. Sem scripts ou executáveis de linha de comando expostos

## 3. Incompatibilidades Identificadas

1. **Mecanismo de Invocação**:
   - Task-scheduler: Executa comandos via `ProcessBuilder`
   - Batch-low-platform: Expõe API REST para iniciar jobs

2. **Passagem de Parâmetros**:
   - Task-scheduler: Passa parâmetros via linha de comando
   - Batch-low-platform: Aceita parâmetros via `JobParameters` em uma chamada REST

3. **Ambiente de Execução**:
   - Task-scheduler: Espera executar em um ambiente com acesso ao sistema de arquivos
   - Batch-low-platform: Executa em um contêiner Docker isolado

4. **Monitoramento**:
   - Task-scheduler: Monitora saída padrão e código de saída
   - Batch-low-platform: Fornece status via API REST

## 4. Opções para Resolução

### Opção 1: Modificar o Batch-Low-Platform

Modificar o batch-low-platform para expor uma interface de linha de comando:

1. Adicionar uma classe `CommandLineRunner` ou `ApplicationRunner`
2. Criar scripts wrapper para invocar a aplicação com parâmetros específicos
3. Expor esses scripts no sistema de arquivos do contêiner

### Opção 2: Adaptar o Task-Scheduler

Modificar o task-scheduler para suportar execução de batch via API REST:

1. Criar um novo executor específico para batch via REST
2. Adaptar o modelo de configuração para incluir URL e parâmetros REST
3. Implementar lógica para monitorar o status do job via polling da API REST

### Opção 3: Solução Híbrida

Criar um script wrapper no sistema host que:

1. Aceita parâmetros de linha de comando
2. Converte esses parâmetros em chamadas REST para o batch-low-platform
3. Monitora o status via API REST
4. Retorna código de saída apropriado

## 5. Recomendação

A **Opção 2** (Adaptar o Task-Scheduler) é a mais alinhada com a arquitetura moderna de microserviços:

1. Criar um novo tipo de tarefa `REST_BATCH` no enum `TaskType`
2. Implementar um novo executor `RestBatchExecutor` que:
   - Inicia jobs via chamada REST para `/api/batch/start`
   - Monitora o status via polling para `/api/batch/status`
   - Traduz respostas HTTP em códigos de status para o task-scheduler

Esta abordagem:
- Mantém a separação de responsabilidades
- Respeita a arquitetura containerizada do batch-low-platform
- Não requer modificações no batch-low-platform
- Permite monitoramento e logging consistentes

## 6. Conclusão

O task-scheduler e o batch-low-platform, em suas implementações atuais, não são compatíveis para o cenário de execução de batch via prompt de comando. O task-scheduler espera executar comandos diretamente no sistema operacional, enquanto o batch-low-platform expõe apenas uma interface REST.

Para alcançar o objetivo de permitir que o task-scheduler execute jobs no batch-low-platform, é necessário adaptar o task-scheduler para suportar a invocação de batch jobs via API REST, ou modificar o batch-low-platform para expor uma interface de linha de comando.