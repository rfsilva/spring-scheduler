# Resumo do Projeto Batch-Low-Platform

## Visão Geral

O projeto `batch-low-platform` foi implementado como uma aplicação Java 21 com Spring Batch 3 projetada exclusivamente para ser executada via linha de comando. Esta implementação é totalmente compatível com o modelo de execução esperado pelo `task-scheduler`, que precisa disparar batch jobs através de comandos do sistema operacional.

## Características Principais

1. **Execução via Linha de Comando**: A aplicação é executada exclusivamente via linha de comando, sem expor endpoints REST ou interfaces web.

2. **Processamento em Lote**: Implementa processamento em lote usando Spring Batch com reader, processor e writer.

3. **Configuração Flexível**: Permite configurar diferentes jobs e parâmetros via argumentos de linha de comando.

4. **Persistência de Dados**: Utiliza banco de dados H2 em arquivo para armazenar dados de entrada e resultados.

5. **Scripts de Execução**: Fornece scripts `run.sh` e `run.bat` para facilitar a execução em diferentes sistemas operacionais.

6. **Utilitário de Carregamento de Dados**: Inclui scripts `load-data.sh` e `load-data.bat` para carregar dados de teste.

7. **Códigos de Saída**: Retorna códigos de saída apropriados (0 para sucesso, 1 para erro) que podem ser capturados pelo task-scheduler.

## Compatibilidade com Task-Scheduler

Esta implementação é totalmente compatível com o `LowPlatformBatchExecutor` do task-scheduler, que:

1. Executa comandos via `ProcessBuilder`
2. Passa parâmetros via linha de comando
3. Define diretório de trabalho
4. Captura saída padrão e código de saída
5. Executa comandos adicionais em caso de sucesso ou falha

## Exemplo de Configuração no Task-Scheduler

```
Comando: /path/to/batch-low-platform/run.sh
Parâmetros: --job=processJob --param status=PENDING --param operation=transform
Diretório de trabalho: /path/to/batch-low-platform
Timeout: 300
```

## Estrutura do Projeto

```
batch-low-platform/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── batch/
│       │               └── low/
│       │                   ├── BatchLowPlatformApplication.java  # Aplicação principal com CommandLineRunner
│       │                   ├── config/
│       │                   │   └── BatchConfig.java              # Configuração dos jobs e steps
│       │                   ├── model/
│       │                   │   ├── BatchData.java                # Modelo de dados de entrada
│       │                   │   └── BatchResult.java              # Modelo de resultados
│       │                   ├── processor/
│       │                   │   └── BatchItemProcessor.java       # Processador de itens
│       │                   ├── reader/
│       │                   │   └── BatchItemReader.java          # Leitor de itens
│       │                   ├── repository/
│       │                   │   ├── BatchDataRepository.java      # Repositório de dados
│       │                   │   └── BatchResultRepository.java    # Repositório de resultados
│       │                   ├── util/
│       │                   │   └── DataLoader.java               # Utilitário para carregar dados de teste
│       │                   └── writer/
│       │                       └── BatchItemWriter.java          # Escritor de resultados
│       └── resources/
│           ├── application.properties                            # Configuração principal
│           ├── application-dataloader.properties                 # Configuração para carregador de dados
│           ├── data.sql                                          # Dados iniciais
│           └── schema.sql                                        # Esquema do banco de dados
├── .gitignore                                                    # Arquivos a serem ignorados pelo Git
├── Dockerfile                                                    # Configuração para Docker
├── load-data.bat                                                 # Script para carregar dados (Windows)
├── load-data.sh                                                  # Script para carregar dados (Linux/Mac)
├── mvnw                                                          # Maven Wrapper para Linux/Mac
├── mvnw.cmd                                                      # Maven Wrapper para Windows
├── pom.xml                                                       # Configuração do Maven
├── README.md                                                     # Documentação
├── run.bat                                                       # Script de execução para Windows
└── run.sh                                                        # Script de execução para Linux/Mac
```

## Conclusão

O projeto `batch-low-platform` foi implementado seguindo as melhores práticas para aplicações batch em linha de comando, garantindo total compatibilidade com o modelo de execução esperado pelo `task-scheduler`. A aplicação não expõe endpoints REST ou interfaces web, focando exclusivamente na execução via linha de comando, conforme solicitado.