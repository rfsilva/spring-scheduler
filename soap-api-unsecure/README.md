# SOAP API Unsecure

Este é um serviço SOAP simples sem segurança para testes do task-scheduler.

## Compilação

Para compilar o projeto corretamente, use os scripts fornecidos:

- Windows: `build.bat`
- Linux/Mac: `./build.sh` (certifique-se de dar permissão de execução com `chmod +x build.sh`)

Estes scripts garantem que as classes Java sejam geradas a partir do XSD antes da compilação.

## Endpoints

### SOAP Endpoints

O serviço WSDL está disponível em: http://localhost:8084/ws/calculator.wsdl

Operações disponíveis:
- add - Soma dois números
- subtract - Subtrai dois números
- multiply - Multiplica dois números
- divide - Divide dois números
- status - Retorna o status do serviço

### REST Endpoints

- GET /api/health - Verifica a saúde do serviço

## Exemplos de Requisições SOAP

### Exemplo de requisição Add

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:addRequest>
         <cal:a>10</cal:a>
         <cal:b>20</cal:b>
      </cal:addRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### Exemplo de requisição Status

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cal="http://example.com/soap/calculator">
   <soapenv:Header/>
   <soapenv:Body>
      <cal:statusRequest/>
   </soapenv:Body>
</soapenv:Envelope>
```