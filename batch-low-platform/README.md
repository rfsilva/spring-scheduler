# Batch Low Platform

Uma aplicação de processamento em lote (batch) projetada para ser executada via linha de comando, compatível com o task-scheduler.

## Requisitos

- Java 21
- Maven 3.8+

## Compilação

```bash
./mvnw clean package
```

## Execução

A aplicação pode ser executada diretamente via linha de comando:

### Linux/Mac:

```bash
./run.sh --job=processJob --param status=PENDING --param operation=transform
```

### Windows:

```bash
run.bat --job=processJob --param status=PENDING --param operation=transform
```

### Diretamente com Java:

```bash
java -jar target/batch-low-platform-0.0.1-SNAPSHOT.jar --job=processJob --param status=PENDING --param operation=transform
```

## Parâmetros Disponíveis

| Parâmetro | Descrição | Obrigatório | Exemplo |
|-----------|-----------|-------------|---------|
| --job, -j | Nome do job a ser executado | Sim | --job=processJob |
| --param, -p | Parâmetros do job no formato chave=valor | Não | --param status=PENDING |
| --help, -h | Exibe ajuda | Não | --help |

## Jobs Disponíveis

1. **processJob**: Job genérico de processamento
2. **transformJob**: Job específico para transformação de dados
3. **validateJob**: Job específico para validação de dados

## Parâmetros de Job

| Parâmetro | Descrição | Valores | Padrão |
|-----------|-----------|---------|--------|
| status | Status dos registros a serem processados | String | PENDING |
| operation | Operação a ser realizada | transform, calculate, validate | transform |
| factor | Fator de multiplicação (para operação calculate) | Double | 1.0 |
| updateSource | Atualizar status do registro de origem | true, false | true |

## Exemplos de Uso

### Processar registros pendentes com transformação:

```bash
./run.sh --job=processJob --param status=PENDING --param operation=transform
```

### Calcular valores com fator de multiplicação:

```bash
./run.sh --job=processJob --param status=PENDING --param operation=calculate --param factor=2.5
```

### Validar registros sem atualizar status de origem:

```bash
./run.sh --job=validateJob --param status=PENDING --param updateSource=false
```

## Carregamento de Dados de Teste

Para facilitar os testes, a aplicação inclui um utilitário para carregar dados de teste:

### Linux/Mac:

```bash
./load-data.sh --count=20 --prefix="test-" --status="PENDING"
```

### Windows:

```bash
load-data.bat --count=20 --prefix="test-" --status="PENDING"
```

### Parâmetros do Carregador de Dados:

| Parâmetro | Descrição | Obrigatório | Padrão |
|-----------|-----------|-------------|--------|
| --count, -c | Número de registros a gerar | Não | 10 |
| --prefix, -p | Prefixo para os dados gerados | Não | test-data- |
| --status, -s | Status dos registros gerados | Não | PENDING |
| --help, -h | Exibe ajuda | Não | - |

## Integração com Task-Scheduler

Para configurar o task-scheduler para executar este batch:

1. Configure uma tarefa do tipo `LOW_PLATFORM_BATCH`
2. Defina o comando como o caminho para o script `run.sh` ou `run.bat`
3. Defina os parâmetros conforme necessário (ex: `--job=processJob --param status=PENDING`)
4. Configure o diretório de trabalho para o diretório onde o batch está instalado

Exemplo de configuração no task-scheduler:

```
Comando: /path/to/batch-low-platform/run.sh
Parâmetros: --job=processJob --param status=PENDING --param operation=transform
Diretório de trabalho: /path/to/batch-low-platform
Timeout: 300
```

## Códigos de Saída

- **0**: Execução bem-sucedida
- **1**: Erro na execução (detalhes no log)

## Logs

Os logs são gravados em:
- Console
- Arquivo: `logs/batch-low-platform.log`

## Banco de Dados

A aplicação utiliza um banco de dados H2 em arquivo para armazenar os dados:
- URL: `jdbc:h2:file:./batch-db`
- Usuário: `sa`
- Senha: `password`

Os dados são persistidos entre execuções, permitindo processamento incremental.

## Docker

A aplicação também pode ser executada via Docker:

```bash
# Construir a imagem
docker build -t batch-low-platform .

# Executar um job
docker run -v $(pwd)/logs:/app/logs -v $(pwd)/data:/app/data batch-low-platform --job=processJob --param status=PENDING
```