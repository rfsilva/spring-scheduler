# Relatório de Compatibilidade do Task-Scheduler

Este relatório analisa a compatibilidade do task-scheduler com os cinco serviços de teste criados (batch-low-platform, rest-api-secure, rest-api-unsecure, soap-api-secure e soap-api-unsecure) e identifica possíveis ajustes necessários para garantir a integração adequada.

## Resumo Executivo

O task-scheduler é geralmente compatível com os serviços criados, mas existem algumas áreas que precisam de atenção para garantir uma integração perfeita, especialmente em relação à autenticação e manipulação de cabeçalhos específicos.

## 1. Compatibilidade com REST API Unsecure

### Status: ✅ Totalmente Compatível

O task-scheduler pode se comunicar com o serviço rest-api-unsecure sem problemas, pois:

- O serviço não requer autenticação
- O task-scheduler suporta todos os métodos HTTP necessários (GET, POST, PUT, DELETE)
- O task-scheduler pode enviar e processar JSON corretamente

**Nenhuma alteração necessária.**

## 2. Compatibilidade com REST API Secure

### Status: ⚠️ Parcialmente Compatível

O task-scheduler pode se comunicar com o serviço rest-api-secure, mas requer configuração específica:

- O serviço usa autenticação JWT Bearer Token
- O task-scheduler suporta autenticação Bearer Token através do tipo `BEARER` no enum `RestAuthType`
- **Problema potencial**: O task-scheduler espera que o token seja fornecido diretamente no campo `clientSecret`, mas não tem um fluxo para obter o token através do endpoint de autenticação

**Ajustes recomendados:**
1. Implementar um mecanismo para obter automaticamente o token JWT do endpoint `/auth/login` antes de fazer chamadas para endpoints protegidos
2. Alternativamente, documentar claramente que o usuário deve obter o token manualmente e configurá-lo no campo `clientSecret`

## 3. Compatibilidade com SOAP API Unsecure

### Status: ✅ Totalmente Compatível

O task-scheduler pode se comunicar com o serviço soap-api-unsecure sem problemas, pois:

- O serviço não requer autenticação
- O task-scheduler suporta o envio de requisições SOAP com os cabeçalhos corretos
- O task-scheduler pode processar respostas XML corretamente

**Nenhuma alteração necessária.**

## 4. Compatibilidade com SOAP API Secure

### Status: ✅ Totalmente Compatível

O task-scheduler pode se comunicar com o serviço soap-api-secure, pois:

- O serviço usa autenticação básica HTTP
- O task-scheduler suporta autenticação básica através do tipo `BASIC` no enum `SoapAuthType`
- O task-scheduler tem campos para username e password que são usados corretamente para autenticação básica

**Nenhuma alteração necessária.**

## 5. Compatibilidade com Batch Low Platform

### Status: ❌ Incompatível

O task-scheduler tem um executor para tarefas batch de baixa plataforma, mas há uma incompatibilidade fundamental:

- O serviço batch-low-platform expõe uma API REST para iniciar jobs batch
- O executor `LowPlatformBatchExecutor` do task-scheduler está projetado para executar comandos locais do sistema operacional, não para chamar APIs REST

**Ajustes necessários:**
1. Criar um novo tipo de tarefa específico para batch via API REST ou
2. Modificar o `LowPlatformBatchExecutor` para suportar tanto execução local quanto chamadas de API REST ou
3. Documentar que para interagir com o batch-low-platform, deve-se usar o `RestTaskExecutor` em vez do `LowPlatformBatchExecutor`

## Recomendações Detalhadas

### 1. Melhorias na Autenticação JWT

O task-scheduler não tem um mecanismo para obter tokens JWT automaticamente. Recomendamos:

```java
// Adicionar um método para obter token JWT
private String obtainJwtToken(RestTaskConfig config) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
        HttpPost request = new HttpPost(config.getTokenEndpoint());
        request.setHeader("Content-Type", "application/json");
        
        // Criar payload de login
        String loginPayload = String.format(
            "{\"username\":\"%s\",\"password\":\"%s\"}", 
            config.getClientId(), 
            config.getClientSecret()
        );
        
        StringEntity entity = new StringEntity(loginPayload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() == 200) {
                String responseBody = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                    .lines().collect(Collectors.joining("\n"));
                
                // Extrair token do JSON de resposta
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseBody);
                return root.path("token").asText();
            } else {
                throw new RuntimeException("Failed to obtain JWT token: " + response.getCode());
            }
        }
    } catch (Exception e) {
        throw new RuntimeException("Error obtaining JWT token", e);
    }
}
```

### 2. Suporte a Batch via API REST

Para suportar o serviço batch-low-platform, recomendamos adicionar um novo tipo de executor:

```java
@Component
@DisallowConcurrentExecution
@Slf4j
public class RestBatchExecutor implements Job {
    // Implementação similar ao RestTaskExecutor mas específica para APIs de batch
}
```

Ou adicionar um novo tipo de tarefa no enum `TaskType`:

```java
public enum TaskType {
    REST_CALL,
    SOAP_CALL,
    LOW_PLATFORM_BATCH,
    HIGH_PLATFORM_BATCH,
    MESSAGING,
    REST_BATCH  // Novo tipo para batch via API REST
}
```

### 3. Melhorias na Documentação

Recomendamos adicionar documentação clara sobre:

1. Como configurar tarefas para interagir com serviços que requerem autenticação JWT
2. Como usar o `RestTaskExecutor` para interagir com o serviço batch-low-platform
3. Exemplos específicos de configuração para cada um dos serviços de teste

## Conclusão

O task-scheduler é amplamente compatível com os serviços REST e SOAP criados, tanto seguros quanto não seguros. A principal incompatibilidade está no serviço batch-low-platform, que requer uma abordagem diferente da implementada atualmente no executor de batch.

Com as modificações sugeridas, o task-scheduler poderá interagir perfeitamente com todos os cinco serviços de teste, proporcionando um ambiente de teste abrangente para diferentes cenários de integração.