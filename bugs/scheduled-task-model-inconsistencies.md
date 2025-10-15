# Análise de Inconsistências entre Modelos Front-end e Back-end

Este documento apresenta uma análise detalhada das diferenças entre o modelo `ScheduledTask` do front-end, o componente `task-form.component` e os DTOs do back-end, bem como um plano de ajuste para corrigir esses problemas.

## Análise de Diferenças entre Front-end e Back-end

### 1. Diferenças no Modelo Principal (ScheduledTask)

#### Campos Básicos
- **Front-end e Back-end estão alinhados** nos campos básicos: id, name, description, taskType, status, cronExpression, maxRetries, retryDelaySeconds

### 2. Diferenças nas Configurações Específicas

#### 2.1. RestTaskConfig

| Campo | Front-end | Back-end | Problema |
|-------|-----------|----------|----------|
| url | ✅ | ✅ | - |
| method | ✅ | ✅ | - |
| headers | ✅ (opcional) | ✅ (obrigatório) | Front-end não marca como obrigatório |
| body | ✅ (opcional) | ✅ (opcional) | - |
| authType | ✅ | ✅ | - |
| username | ✅ | ❌ | Campo extra no front-end |
| password | ✅ | ❌ | Campo extra no front-end |
| token | ✅ | ❌ | Campo extra no front-end |
| apiKeyName | ✅ | ❌ | Campo extra no front-end |
| apiKeyValue | ✅ | ❌ | Campo extra no front-end |
| connectTimeoutSeconds | ✅ | ❌ | Campo extra no front-end |
| readTimeoutSeconds | ✅ | ❌ | Campo extra no front-end |
| timeout | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| retryPolicy | ❌ | ✅ | Campo ausente no front-end |
| tokenEndpoint | ❌ | ✅ | Campo ausente no front-end |
| clientId | ❌ | ✅ | Campo ausente no front-end |
| clientSecret | ❌ | ✅ | Campo ausente no front-end |
| certificatePath | ❌ | ✅ | Campo ausente no front-end |

#### 2.2. SoapTaskConfig

| Campo | Front-end | Back-end | Problema |
|-------|-----------|----------|----------|
| endpointUrl | ✅ | ❌ | Nome diferente (wsdlUrl no back-end) |
| soapAction | ✅ (opcional) | ✅ (opcional) | - |
| requestXml | ✅ | ✅ | - |
| authType | ✅ | ✅ | - |
| username | ✅ (opcional) | ✅ (obrigatório) | Front-end não marca como obrigatório |
| password | ✅ (opcional) | ✅ (obrigatório) | Front-end não marca como obrigatório |
| connectTimeoutSeconds | ✅ | ❌ | Campo extra no front-end |
| readTimeoutSeconds | ✅ | ❌ | Campo extra no front-end |
| wsdlUrl | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| operation | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| namespace | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| timeout | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| customHeaders | ❌ | ✅ | Campo ausente no front-end |

#### 2.3. LowPlatformBatchConfig

| Campo | Front-end | Back-end | Problema |
|-------|-----------|----------|----------|
| command | ✅ | ✅ | - |
| workingDirectory | ✅ (opcional) | ✅ (obrigatório) | Front-end não marca como obrigatório |
| environmentVariables | ✅ | ❌ | Campo extra no front-end |
| successExitCodes | ✅ | ❌ | Campo extra no front-end |
| onSuccessCommand | ✅ | ❌ | Nome diferente (onSuccess no back-end) |
| onFailureCommand | ✅ | ❌ | Nome diferente (onFailure no back-end) |
| timeoutSeconds | ✅ | ❌ | Nome diferente (timeout no back-end) |
| jobName | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| parameters | ❌ | ✅ | Campo ausente no front-end |
| timeout | ❌ | ✅ (obrigatório) | Nome diferente (timeoutSeconds no front-end) |
| runAsUser | ❌ | ✅ | Campo ausente no front-end |
| onSuccess | ❌ | ✅ | Nome diferente (onSuccessCommand no front-end) |
| onFailure | ❌ | ✅ | Nome diferente (onFailureCommand no front-end) |

#### 2.4. HighPlatformBatchConfig

| Campo | Front-end | Back-end | Problema |
|-------|-----------|----------|----------|
| systemType | ✅ | ❌ | Campo extra no front-end |
| jobName | ✅ | ❌ | Campo extra no front-end |
| parameters | ✅ | ❌ | Campo extra no front-end |
| credentials | ✅ | ✅ | - |
| connectionDetails | ✅ | ❌ | Campo extra no front-end |
| timeoutSeconds | ✅ | ❌ | Nome diferente (timeout no back-end) |
| endpointType | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| transactionId | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| payload | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| timeout | ❌ | ✅ (obrigatório) | Nome diferente (timeoutSeconds no front-end) |
| channel | ❌ | ✅ | Campo ausente no front-end |
| queue | ❌ | ✅ | Campo ausente no front-end |
| host | ❌ | ✅ | Campo ausente no front-end |
| port | ❌ | ✅ | Campo ausente no front-end |
| sslCertPath | ❌ | ✅ | Campo ausente no front-end |

#### 2.5. MessagingTaskConfig

| Campo | Front-end | Back-end | Problema |
|-------|-----------|----------|----------|
| brokerType | ✅ | ✅ | - |
| destination | ✅ | ❌ | Nome diferente (destinationName no back-end) |
| message | ✅ | ❌ | Nome diferente (messagePayload no back-end) |
| connectionProperties | ✅ | ❌ | Nome diferente (connectionUrl no back-end) |
| authType | ✅ | ✅ | - |
| username | ✅ | ❌ | Campo extra no front-end |
| password | ✅ | ❌ | Campo extra no front-end |
| deliveryMode | ✅ | ✅ | - |
| timeoutSeconds | ✅ | ❌ | Campo extra no front-end |
| destinationName | ❌ | ✅ (obrigatório) | Nome diferente (destination no front-end) |
| messagePayload | ❌ | ✅ (obrigatório) | Nome diferente (message no front-end) |
| connectionUrl | ❌ | ✅ (obrigatório) | Nome diferente (connectionProperties no front-end) |
| headers | ❌ | ✅ (obrigatório) | Campo ausente no front-end |
| retryPolicy | ❌ | ✅ | Campo ausente no front-end |

### 3. Problemas de Validação

1. O front-end não está validando campos obrigatórios conforme definido no back-end
2. Há campos no front-end que não existem no back-end
3. Há campos no back-end que não existem no front-end
4. Alguns campos têm nomes diferentes entre front-end e back-end
5. O front-end não implementa as validações específicas que existem no back-end (ex: validação de RestTaskConfig e HighPlatformBatchConfig)

## Plano de Ajuste

### 1. Atualização do Modelo ScheduledTask no Front-end

#### 1.1. RestTaskConfig
```typescript
export interface RestTaskConfig {
  id?: number;
  url: string;
  method: HttpMethod;
  headers: string; // Agora obrigatório
  body?: string;
  authType: RestAuthType;
  timeout: number; // Novo campo obrigatório
  retryPolicy?: string; // Novo campo
  tokenEndpoint?: string; // Novo campo
  clientId?: string; // Novo campo
  clientSecret?: string; // Novo campo
  certificatePath?: string; // Novo campo
  // Remover campos não utilizados pelo back-end
}
```

#### 1.2. SoapTaskConfig
```typescript
export interface SoapTaskConfig {
  id?: number;
  wsdlUrl: string; // Renomeado de endpointUrl
  operation: string; // Novo campo obrigatório
  namespace: string; // Novo campo obrigatório
  soapAction?: string;
  requestXml: string;
  authType: SoapAuthType;
  username: string; // Agora obrigatório
  password: string; // Agora obrigatório
  timeout: number; // Novo campo obrigatório
  customHeaders?: string; // Novo campo
  // Remover campos não utilizados pelo back-end
}
```

#### 1.3. LowPlatformBatchConfig
```typescript
export interface LowPlatformBatchConfig {
  id?: number;
  jobName: string; // Novo campo obrigatório
  command: string;
  parameters?: string; // Novo campo
  workingDirectory: string; // Agora obrigatório
  timeout: number; // Renomeado de timeoutSeconds
  runAsUser?: string; // Novo campo
  onSuccess?: string; // Renomeado de onSuccessCommand
  onFailure?: string; // Renomeado de onFailureCommand
  // Remover campos não utilizados pelo back-end
}
```

#### 1.4. HighPlatformBatchConfig
```typescript
export interface HighPlatformBatchConfig {
  id?: number;
  endpointType: EndpointType; // Novo campo obrigatório (enum)
  transactionId: string; // Novo campo obrigatório
  payload: string; // Novo campo obrigatório
  credentials: string;
  timeout: number; // Renomeado de timeoutSeconds
  channel?: string; // Novo campo
  queue?: string; // Novo campo
  host?: string; // Novo campo
  port?: string; // Novo campo
  sslCertPath?: string; // Novo campo
  // Remover campos não utilizados pelo back-end
}
```

#### 1.5. MessagingTaskConfig
```typescript
export interface MessagingTaskConfig {
  id?: number;
  brokerType: BrokerType;
  destinationName: string; // Renomeado de destination
  messagePayload: string; // Renomeado de message
  connectionUrl: string; // Renomeado de connectionProperties
  authType: MessagingAuthType;
  headers: string; // Novo campo obrigatório
  deliveryMode: DeliveryMode;
  retryPolicy?: string; // Novo campo
  // Remover campos não utilizados pelo back-end
}
```

### 2. Atualização do Formulário (task-form.component.ts)

1. Atualizar a estrutura do formulário para refletir as mudanças nos modelos
2. Adicionar validadores para campos obrigatórios
3. Implementar validações específicas conforme definido no back-end
4. Atualizar o método `prepareTaskData()` para mapear corretamente os campos

### 3. Implementação de Validações Específicas

Adicionar validações específicas conforme implementado no back-end:

```typescript
// Para RestTaskConfig
private validateRestTaskConfig(config: RestTaskConfig): boolean {
  if (config.tokenEndpoint) {
    return true;
  } else if (config.clientId && config.clientSecret) {
    return true;
  } else {
    this.snackBar.open('Client ID e Client Secret são obrigatórios quando Token Endpoint não é fornecido', 'Fechar', { duration: 3000 });
    return false;
  }
}

// Para HighPlatformBatchConfig
private validateHighPlatformBatchConfig(config: HighPlatformBatchConfig): boolean {
  switch (config.endpointType) {
    case EndpointType.MQ:
      if (!config.channel || !config.queue) {
        this.snackBar.open('Channel e Queue são obrigatórios para o tipo de endpoint MQ', 'Fechar', { duration: 3000 });
        return false;
      }
      break;
    case EndpointType.API:
      if (!config.host || !config.port || !config.sslCertPath) {
        this.snackBar.open('Host, Port e SSL Certificate Path são obrigatórios para o tipo de endpoint API', 'Fechar', { duration: 3000 });
        return false;
      }
      break;
  }
  return true;
}
```

### 4. Atualização do HTML do Formulário

Atualizar o HTML do formulário para incluir os novos campos e aplicar as validações corretas.

## Estimativa de Esforço e Prazo

### Esforço por Tarefa

1. **Atualização dos Modelos**: 4 horas
   - Revisar e atualizar todas as interfaces
   - Adicionar novos enums necessários
   - Documentar as mudanças

2. **Atualização do Formulário (TS)**: 8 horas
   - Refatorar a estrutura do formulário
   - Implementar validações específicas
   - Atualizar métodos de preparação de dados

3. **Atualização do HTML do Formulário**: 6 horas
   - Adicionar novos campos
   - Atualizar mensagens de validação
   - Ajustar layout para acomodar novos campos

4. **Testes**: 6 horas
   - Testar todas as combinações de tipos de tarefas
   - Verificar validações
   - Testar integração com o back-end

5. **Correção de Bugs**: 4 horas
   - Resolver problemas identificados durante os testes

### Estimativa Total

- **Esforço Total**: 28 horas de trabalho
- **Prazo**: 4-5 dias úteis (considerando um desenvolvedor trabalhando em tempo integral)

### Considerações Adicionais

1. **Impacto em Tarefas Existentes**: Será necessário verificar se as tarefas já cadastradas no sistema continuarão funcionando após as mudanças.

2. **Migração de Dados**: Pode ser necessário criar um script para migrar dados existentes para o novo formato.

3. **Documentação**: Recomenda-se atualizar a documentação do sistema para refletir as mudanças realizadas.

4. **Testes de Regressão**: Importante realizar testes de regressão para garantir que outras funcionalidades não sejam afetadas.