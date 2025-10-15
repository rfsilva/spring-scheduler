# Análise de Obrigatoriedades e Validações Condicionais

Este documento apresenta uma análise detalhada das obrigatoriedades dos campos e validações condicionais implementadas no front-end para cada tipo de tarefa agendada.

## 1. Campos Básicos (Comuns a Todos os Tipos)

| Campo | Obrigatoriedade | Validação |
|-------|----------------|-----------|
| name | Obrigatório | Validador padrão do Angular |
| description | Obrigatório | Validador padrão do Angular |
| taskType | Obrigatório | Validador padrão do Angular |
| status | Obrigatório | Validador padrão do Angular |
| cronExpression | Obrigatório | Validador padrão do Angular |
| maxRetries | Opcional | - |
| retryDelaySeconds | Opcional | - |

## 2. REST Task Config

### 2.1. Campos Obrigatórios Simples

| Campo | Obrigatoriedade | Validação |
|-------|----------------|-----------|
| url | Obrigatório | Validador padrão do Angular |
| method | Obrigatório | Validador padrão do Angular |
| headers | Obrigatório | Validador padrão do Angular |
| authType | Obrigatório | Validador padrão do Angular |
| timeout | Obrigatório | Validador padrão do Angular |

### 2.2. Validações Condicionais

Para tarefas REST com autenticação do tipo BEARER, existe uma validação condicional:

```typescript
// Método para validar RestTaskConfig
validateRestTaskConfig(): boolean {
  const config = this.taskForm.get('restTaskConfig')?.value;
  if (!config) return false;

  if (config.tokenEndpoint) {
    return true;
  } else if (config.clientId && config.clientSecret) {
    return true;
  } else if (config.authType === RestAuthType.BEARER) {
    this.snackBar.open('Client ID e Client Secret são obrigatórios quando Token Endpoint não é fornecido', 'Fechar', { duration: 3000 });
    return false;
  }
  return true;
}
```

**Regra implementada:**
- Se `tokenEndpoint` for informado, não há requisitos adicionais
- Se `tokenEndpoint` não for informado, mas `clientId` e `clientSecret` forem informados, a validação passa
- Se `tokenEndpoint` não for informado e `clientId` ou `clientSecret` não forem informados, e o `authType` for BEARER, a validação falha

Esta regra está alinhada com a validação do back-end em `ScheduledTaskService.validateRestTaskConfig()`:

```java
private void validateRestTaskConfig(RestTaskConfigDTO config) {
    // Validate token endpoint, clientId, and clientSecret based on rules
    if (config.getTokenEndpoint() == null) {
        if (config.getClientId() == null || config.getClientSecret() == null) {
            throw new IllegalArgumentException("Client ID and Client Secret are required when Token Endpoint is not provided");
        }
    }
}
```

## 3. SOAP Task Config

### 3.1. Campos Obrigatórios

| Campo | Obrigatoriedade | Validação |
|-------|----------------|-----------|
| wsdlUrl | Obrigatório | Validador padrão do Angular |
| operation | Obrigatório | Validador padrão do Angular |
| namespace | Obrigatório | Validador padrão do Angular |
| requestXml | Obrigatório | Validador padrão do Angular |
| authType | Obrigatório | Validador padrão do Angular |
| username | Obrigatório | Validador padrão do Angular |
| password | Obrigatório | Validador padrão do Angular |
| timeout | Obrigatório | Validador padrão do Angular |

Não há validações condicionais específicas para SOAP Task Config além das validações padrão de campos obrigatórios.

## 4. Low Platform Batch Config

### 4.1. Campos Obrigatórios

| Campo | Obrigatoriedade | Validação |
|-------|----------------|-----------|
| jobName | Obrigatório | Validador padrão do Angular |
| command | Obrigatório | Validador padrão do Angular |
| workingDirectory | Obrigatório | Validador padrão do Angular |
| timeout | Obrigatório | Validador padrão do Angular |

Não há validações condicionais específicas para Low Platform Batch Config além das validações padrão de campos obrigatórios.

## 5. High Platform Batch Config

### 5.1. Campos Obrigatórios Simples

| Campo | Obrigatoriedade | Validação |
|-------|----------------|-----------|
| endpointType | Obrigatório | Validador padrão do Angular |
| transactionId | Obrigatório | Validador padrão do Angular |
| payload | Obrigatório | Validador padrão do Angular |
| credentials | Obrigatório | Validador padrão do Angular |
| timeout | Obrigatório | Validador padrão do Angular |

### 5.2. Validações Condicionais

Para High Platform Batch Config, existem validações condicionais baseadas no tipo de endpoint:

```typescript
// Método para atualizar validadores específicos para HighPlatformBatchConfig
updateHighPlatformValidators(endpointType: EndpointType): void {
  const channelControl = this.taskForm.get('highPlatformBatchConfig.channel');
  const queueControl = this.taskForm.get('highPlatformBatchConfig.queue');
  const hostControl = this.taskForm.get('highPlatformBatchConfig.host');
  const portControl = this.taskForm.get('highPlatformBatchConfig.port');
  const sslCertPathControl = this.taskForm.get('highPlatformBatchConfig.sslCertPath');

  // Resetar validadores
  channelControl?.clearValidators();
  queueControl?.clearValidators();
  hostControl?.clearValidators();
  portControl?.clearValidators();
  sslCertPathControl?.clearValidators();

  // Aplicar validadores específicos com base no tipo de endpoint
  if (endpointType === EndpointType.MQ) {
    channelControl?.setValidators([Validators.required]);
    queueControl?.setValidators([Validators.required]);
  } else if (endpointType === EndpointType.API) {
    hostControl?.setValidators([Validators.required]);
    portControl?.setValidators([Validators.required]);
    sslCertPathControl?.setValidators([Validators.required]);
  }

  // Atualizar estado dos controles
  channelControl?.updateValueAndValidity();
  queueControl?.updateValueAndValidity();
  hostControl?.updateValueAndValidity();
  portControl?.updateValueAndValidity();
  sslCertPathControl?.updateValueAndValidity();
}
```

**Regras implementadas:**
- Se `endpointType` for `MQ`, os campos `channel` e `queue` são obrigatórios
- Se `endpointType` for `API`, os campos `host`, `port` e `sslCertPath` são obrigatórios
- Para outros tipos de endpoint, não há campos adicionais obrigatórios

Estas regras estão alinhadas com a validação do back-end em `ScheduledTaskService.validateHighPlatformBatchConfig()`:

```java
private void validateHighPlatformBatchConfig(HighPlatformBatchConfigDTO config) {
    // Validate based on endpoint type
    switch (config.getEndpointType()) {
        case MQ:
            if (config.getChannel() == null || config.getQueue() == null) {
                throw new IllegalArgumentException("Channel and Queue are required for MQ endpoint type");
            }
            break;
        case API:
            if (config.getHost() == null || config.getPort() == null || config.getSslCertPath() == null) {
                throw new IllegalArgumentException("Host, Port, and SSL Certificate Path are required for API endpoint type");
            }
            break;
        default:
            // No specific validation for other endpoint types
            break;
    }
}
```

## 6. Messaging Task Config

### 6.1. Campos Obrigatórios

| Campo | Obrigatoriedade | Validação |
|-------|----------------|-----------|
| brokerType | Obrigatório | Validador padrão do Angular |
| destinationName | Obrigatório | Validador padrão do Angular |
| messagePayload | Obrigatório | Validador padrão do Angular |
| connectionUrl | Obrigatório | Validador padrão do Angular |
| authType | Obrigatório | Validador padrão do Angular |
| headers | Obrigatório | Validador padrão do Angular |
| deliveryMode | Obrigatório | Validador padrão do Angular |

Não há validações condicionais específicas para Messaging Task Config além das validações padrão de campos obrigatórios.

## 7. Implementação das Validações no Formulário

As validações são aplicadas em três níveis:

### 7.1. Validações Declarativas
Aplicadas diretamente na definição do formulário usando `Validators.required`:

```typescript
this.taskForm = this.fb.group({
  name: ['', [Validators.required]],
  // outros campos...
  
  restTaskConfig: this.fb.group({
    url: ['', [Validators.required]],
    // outros campos...
  }),
  
  // outros grupos...
});
```

### 7.2. Validações Dinâmicas
Aplicadas em resposta a mudanças nos valores do formulário:

```typescript
// Adicionar listeners para validações específicas
this.taskForm.get('highPlatformBatchConfig.endpointType')?.valueChanges.subscribe(endpointType => {
  this.updateHighPlatformValidators(endpointType);
});
```

### 7.3. Validações Programáticas
Aplicadas no momento do envio do formulário:

```typescript
onSubmit(): void {
  if (this.taskForm.invalid) {
    this.markFormGroupTouched(this.taskForm);
    this.snackBar.open('Por favor, corrija os erros no formulário', 'Fechar', { duration: 3000 });
    return;
  }

  // Validações específicas com base no tipo de tarefa
  const taskType = this.taskForm.get('taskType')?.value;
  let isValid = true;

  switch (taskType) {
    case TaskType.REST_CALL:
      isValid = this.validateRestTaskConfig();
      break;
    case TaskType.HIGH_PLATFORM_BATCH:
      // A validação já é feita pelos validadores dinâmicos
      break;
  }

  if (!isValid) {
    return;
  }
  
  // Continuar com o envio do formulário...
}
```

## 8. Feedback Visual para o Usuário

O formulário fornece feedback visual para o usuário sobre campos obrigatórios e validações:

1. **Mensagens de erro** são exibidas abaixo dos campos quando eles são tocados e inválidos
2. **Snackbars** são usadas para mostrar mensagens de erro para validações mais complexas
3. **Marcação visual** de campos inválidos com borda vermelha (padrão do Material)

## 9. Conclusão

A implementação atual das validações no front-end está alinhada com as regras de validação do back-end. As validações condicionais para REST Task Config e High Platform Batch Config foram implementadas corretamente, garantindo que:

1. Para REST Task Config com autenticação BEARER:
   - Se `tokenEndpoint` não for informado, `clientId` e `clientSecret` são obrigatórios

2. Para High Platform Batch Config:
   - Se `endpointType` for `MQ`, `channel` e `queue` são obrigatórios
   - Se `endpointType` for `API`, `host`, `port` e `sslCertPath` são obrigatórios

Estas validações garantem que os dados enviados ao back-end estejam em conformidade com as regras de negócio e evitam erros de validação no servidor.