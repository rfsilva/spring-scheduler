# SOAP API Secure

Este projeto implementa uma API SOAP segura usando Java 21 e Spring Boot 3 com autenticação básica HTTP. Ele serve como um ambiente de teste para o task-scheduler, demonstrando como integrar com serviços SOAP que requerem autenticação.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Web Services
- Spring Security
- JAXB para geração de classes a partir de XSD

## Arquitetura de Segurança

### Autenticação Básica HTTP

O projeto implementa autenticação básica HTTP (Basic Authentication), que é um método de autenticação simples e amplamente suportado:

1. **Mecanismo**: Credenciais (nome de usuário e senha) são codificadas em Base64 e enviadas no cabeçalho HTTP `Authorization`
2. **Formato do cabeçalho**: `Authorization: Basic {credenciais_em_base64}`
3. **Configuração**: Credenciais definidas no arquivo `application.properties`
4. **Implementação**: Utiliza o mecanismo padrão de autenticação básica do Spring Security

### Configuração de Segurança

A configuração de segurança é definida na classe `WebSecurityConfig`:

- **Endpoints públicos**: Apenas `/api/health` é acessível sem autenticação
- **Endpoints protegidos**: Todos os outros endpoints, incluindo os endpoints SOAP, requerem autenticação
- **CSRF**: Desabilitado para facilitar as chamadas SOAP
- **Método de autenticação**: Autenticação básica HTTP

### Credenciais

As credenciais padrão são configuradas no arquivo `application.properties`:

- **Nome de usuário**: admin
- **Senha**: password123

## Endpoints da API

### Endpoint SOAP

O serviço WSDL está disponível em: http://localhost:8085/ws/calculator.wsdl

Operações disponíveis (todas requerem autenticação):
- **add** - Soma dois números
- **subtract** - Subtrai dois números
- **multiply** - Multiplica dois números
- **divide** - Divide dois números
- **status** - Retorna o status do serviço e o usuário autenticado

### Endpoint REST

- **GET /api/health** - Verifica a saúde do serviço (não requer autenticação)

## Como Usar

### Compilação

Para compilar o projeto corretamente, use os scripts fornecidos:

- Windows: `build.bat`
- Linux/Mac: `./build.sh` (certifique-se de dar permissão de execução com `chmod +x build.sh`)

Estes scripts garantem que as classes Java sejam geradas a partir do XSD antes da compilação.

### Acessando o Serviço SOAP com Autenticação

#### Usando SoapUI ou Postman

1. Configure a autenticação básica:
   - Username: admin
   - Password: password123

2. Envie uma requisição SOAP para http://localhost:8085/ws

#### Exemplo de Requisição SOAP com Autenticação Básica

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator/secure">
   <soapenv:Header>
      <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
         <wsse:UsernameToken>
            <wsse:Username>admin</wsse:Username>
            <wsse:Password>password123</wsse:Password>
         </wsse:UsernameToken>
      </wsse:Security>
   </soapenv:Header>
   <soapenv:Body>
      <cal:addRequest>
         <cal:a>10</cal:a>
         <cal:b>20</cal:b>
      </cal:addRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

#### Exemplo de Requisição SOAP com Cabeçalho HTTP Authorization

Alternativamente, você pode enviar a autenticação no cabeçalho HTTP:

```
POST /ws HTTP/1.1
Host: localhost:8085
Content-Type: text/xml
Authorization: Basic YWRtaW46cGFzc3dvcmQxMjM=
```

Onde `YWRtaW46cGFzc3dvcmQxMjM=` é a codificação Base64 de "admin:password123".

### Verificando o Status do Serviço

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator/secure">
   <soapenv:Header>
      <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
         <wsse:UsernameToken>
            <wsse:Username>admin</wsse:Username>
            <wsse:Password>password123</wsse:Password>
         </wsse:UsernameToken>
      </wsse:Security>
   </soapenv:Header>
   <soapenv:Body>
      <cal:statusRequest/>
   </soapenv:Body>
</soapenv:Envelope>
```

A resposta incluirá o nome do usuário autenticado:

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:statusResponse xmlns:ns2="http://example.com/soap/calculator/secure">
         <ns2:status>UP</ns2:status>
         <ns2:timestamp>2023-05-20T12:34:56.789Z</ns2:timestamp>
         <ns2:user>admin</ns2:user>
      </ns2:statusResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

## Estrutura do Projeto

```
soap-api-secure/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── soap/
│   │   │               └── secure/
│   │   │                   ├── SoapApiSecureApplication.java    # Classe principal
│   │   │                   ├── config/
│   │   │                   │   ├── WebSecurityConfig.java       # Configuração de segurança
│   │   │                   │   └── WebServiceConfig.java        # Configuração do serviço SOAP
│   │   │                   ├── controller/
│   │   │                   │   └── HealthController.java        # Endpoint de saúde REST
│   │   │                   ├── endpoint/
│   │   │                   │   └── CalculatorEndpoint.java      # Endpoint SOAP
│   │   │                   ├── generated/                       # Classes geradas pelo JAXB
│   │   │                   └── service/
│   │   │                       └── CalculatorService.java       # Lógica de negócio
│   │   └── resources/
│   │       ├── application.properties                           # Configurações da aplicação
│   │       └── xsd/
│   │           └── calculator.xsd                               # Definição do esquema SOAP
├── build.bat                                                    # Script de build para Windows
├── build.sh                                                     # Script de build para Linux/Mac
├── mvnw                                                         # Maven Wrapper para Linux/Mac
├── mvnw.cmd                                                     # Maven Wrapper para Windows
├── pom.xml                                                      # Configuração do Maven
└── README.md                                                    # Este arquivo
```

## Integração com Task-Scheduler

Para integrar este serviço SOAP seguro com o task-scheduler:

1. Configure uma tarefa SOAP com autenticação do tipo `BASIC`
2. Configure o nome de usuário como `admin` e a senha como `password123`
3. Defina o WSDL URL como `http://soap-api-secure:8085/ws/calculator.wsdl`
4. Especifique a operação desejada (add, subtract, multiply, divide ou status)
5. Forneça o XML de requisição apropriado

### Exemplo de Configuração no Task-Scheduler

```sql
INSERT INTO scheduled_tasks (name, description, task_type, status, cron_expression)
VALUES ('Calculadora SOAP Segura', 'Teste de adição usando o serviço SOAP seguro', 'SOAP_CALL', 'ACTIVE', '0 */5 * * * ?');

INSERT INTO soap_task_configs (scheduled_task_id, wsdl_url, operation, namespace, soap_action, request_xml, auth_type, timeout, username, password)
VALUES (
    LAST_INSERT_ID(),
    'http://soap-api-secure:8085/ws/calculator.wsdl',
    'add',
    'http://example.com/soap/calculator/secure',
    'http://example.com/soap/calculator/secure/add',
    '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator/secure">
       <soapenv:Body>
          <cal:addRequest>
             <cal:a>50</cal:a>
             <cal:b>25</cal:b>
          </cal:addRequest>
       </soapenv:Body>
    </soapenv:Envelope>',
    'BASIC',
    30,
    'admin',
    'password123'
);
```

## Desenvolvimento e Testes

Para executar o projeto localmente:

```bash
# Primeiro gere as classes a partir do XSD
./mvnw jaxb2:xjc

# Depois execute a aplicação
./mvnw spring-boot:run
```

## Considerações de Segurança

A autenticação básica HTTP tem algumas limitações:

1. **Transmissão de credenciais**: As credenciais são apenas codificadas em Base64, não criptografadas. Em um ambiente de produção, sempre use HTTPS.
2. **Armazenamento de credenciais**: As credenciais são armazenadas em texto simples no arquivo de configuração. Em um ambiente de produção, considere usar um armazenamento mais seguro.
3. **Sem controle de acesso refinado**: Este exemplo implementa apenas autenticação, sem autorização baseada em papéis.

Este projeto é adequado para testes, mas para um ambiente de produção, considere implementar medidas de segurança adicionais.