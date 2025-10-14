# SOAP API Secure

Este é um serviço SOAP com segurança básica para testes do task-scheduler.

## Compilação

Para compilar o projeto corretamente, use os scripts fornecidos:

- Windows: `build.bat`
- Linux/Mac: `./build.sh` (certifique-se de dar permissão de execução com `chmod +x build.sh`)

Estes scripts garantem que as classes Java sejam geradas a partir do XSD antes da compilação.

## Credenciais

- Username: admin
- Password: password123

## Endpoints

### SOAP Endpoints

O serviço WSDL está disponível em: http://localhost:8085/ws/calculator.wsdl

Operações disponíveis:
- add - Soma dois números
- subtract - Subtrai dois números
- multiply - Multiplica dois números
- divide - Divide dois números
- status - Retorna o status do serviço e o usuário autenticado

### REST Endpoints

- GET /api/health - Verifica a saúde do serviço (não requer autenticação)

## Exemplos de Requisições SOAP

### Exemplo de requisição Add com autenticação básica

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

### Exemplo de requisição Status com autenticação básica

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