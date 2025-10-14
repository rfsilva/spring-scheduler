# Exemplos de Testes para Endpoints REST e SOAP

Este documento contém exemplos de como testar os endpoints REST e SOAP incluídos na massa de dados de teste.

## Testando Endpoints REST

### 1. ViaCEP - Consulta de CEP

**Usando curl:**

```bash
curl -X GET https://viacep.com.br/ws/01001000/json/
```

**Resposta esperada:**

```json
{
  "cep": "01001-000",
  "logradouro": "Praça da Sé",
  "complemento": "lado ímpar",
  "bairro": "Sé",
  "localidade": "São Paulo",
  "uf": "SP",
  "ibge": "3550308",
  "gia": "1004",
  "ddd": "11",
  "siafi": "7107"
}
```

### 2. Brasil API - Feriados Nacionais

**Usando curl:**

```bash
curl -X GET https://brasilapi.com.br/api/feriados/v1/2023
```

**Resposta esperada (parcial):**

```json
[
  {
    "date": "2023-01-01",
    "name": "Confraternização mundial",
    "type": "national"
  },
  {
    "date": "2023-02-20",
    "name": "Carnaval",
    "type": "national"
  },
  ...
]
```

### 3. Awesome API - Cotação de Moedas

**Usando curl:**

```bash
curl -X GET https://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL,GBP-BRL
```

**Resposta esperada:**

```json
{
  "USDBRL": {
    "code": "USD",
    "codein": "BRL",
    "name": "Dólar Americano/Real Brasileiro",
    "high": "5.1746",
    "low": "5.1276",
    "varBid": "-0.0258",
    "pctChange": "-0.5",
    "bid": "5.1276",
    "ask": "5.1282",
    "timestamp": "1621440058",
    "create_date": "2021-05-19 13:47:38"
  },
  "EURBRL": {
    "code": "EUR",
    "codein": "BRL",
    "name": "Euro/Real Brasileiro",
    "high": "6.3108",
    "low": "6.2669",
    "varBid": "-0.0228",
    "pctChange": "-0.36",
    "bid": "6.2669",
    "ask": "6.2699",
    "timestamp": "1621440058",
    "create_date": "2021-05-19 13:47:38"
  },
  "GBPBRL": {
    "code": "GBP",
    "codein": "BRL",
    "name": "Libra Esterlina/Real Brasileiro",
    "high": "7.3230",
    "low": "7.2690",
    "varBid": "-0.0324",
    "pctChange": "-0.44",
    "bid": "7.2690",
    "ask": "7.2740",
    "timestamp": "1621440058",
    "create_date": "2021-05-19 13:47:38"
  }
}
```

### 4. OpenWeatherMap - Previsão do Tempo

**Usando curl (substitua YOUR_API_KEY pela sua chave):**

```bash
curl -X GET "https://api.openweathermap.org/data/2.5/weather?q=Sao%20Paulo,br&appid=YOUR_API_KEY"
```

**Resposta esperada:**

```json
{
  "coord": {
    "lon": -46.6333,
    "lat": -23.5478
  },
  "weather": [
    {
      "id": 800,
      "main": "Clear",
      "description": "clear sky",
      "icon": "01d"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 294.15,
    "feels_like": 293.99,
    "temp_min": 294.15,
    "temp_max": 294.15,
    "pressure": 1016,
    "humidity": 64
  },
  "visibility": 10000,
  "wind": {
    "speed": 2.06,
    "deg": 90
  },
  "clouds": {
    "all": 0
  },
  "dt": 1621440058,
  "sys": {
    "type": 1,
    "id": 8394,
    "country": "BR",
    "sunrise": 1621415405,
    "sunset": 1621455063
  },
  "timezone": -10800,
  "id": 3448439,
  "name": "São Paulo",
  "cod": 200
}
```

## Testando API REST Não Segura

### 1. Verificar Status da API

**Usando curl:**

```bash
curl -X GET http://localhost:8081/api/health
```

**Resposta esperada:**

```json
{
  "status": "UP",
  "service": "Unsecure REST API"
}
```

### 2. Listar Tarefas

**Usando curl:**

```bash
curl -X GET http://localhost:8081/api/tasks
```

**Resposta esperada:**

```json
[
  {
    "id": 1,
    "title": "Implementar API",
    "description": "Criar endpoints REST",
    "completed": true,
    "createdAt": "2023-01-15T10:00:00",
    "updatedAt": "2023-01-15T14:30:00"
  },
  {
    "id": 2,
    "title": "Testar API",
    "description": "Realizar testes de integração",
    "completed": false,
    "createdAt": "2023-01-16T09:00:00",
    "updatedAt": "2023-01-16T09:00:00"
  }
]
```

### 3. Criar Nova Tarefa

**Usando curl:**

```bash
curl -X POST http://localhost:8081/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Nova Tarefa", "description": "Descrição da nova tarefa", "completed": false}'
```

**Resposta esperada:**

```json
{
  "id": 3,
  "title": "Nova Tarefa",
  "description": "Descrição da nova tarefa",
  "completed": false,
  "createdAt": "2023-05-20T15:30:45.123",
  "updatedAt": "2023-05-20T15:30:45.123"
}
```

## Testando API REST Segura (com H2 Database)

### 1. Verificar Status da API

**Usando curl:**

```bash
curl -X GET http://localhost:8082/api/health
```

**Resposta esperada:**

```json
{
  "status": "UP",
  "service": "Secure REST API"
}
```

### 2. Autenticar e Obter Token JWT

**Usando curl:**

```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "password"}'
```

**Resposta esperada:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "user",
  "email": "user@example.com",
  "roles": ["USER"]
}
```

### 3. Listar Tarefas (Autenticado)

**Usando curl (substitua YOUR_JWT_TOKEN pelo token obtido no passo anterior):**

```bash
curl -X GET http://localhost:8082/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Resposta esperada:**

```json
[
  {
    "id": 1,
    "title": "Implement authentication",
    "description": "Add JWT authentication to the API",
    "completed": true,
    "createdAt": "2023-05-20T10:00:00",
    "updatedAt": "2023-05-20T14:30:00"
  },
  {
    "id": 2,
    "title": "Create task endpoints",
    "description": "Implement CRUD operations for tasks",
    "completed": true,
    "createdAt": "2023-05-21T09:00:00",
    "updatedAt": "2023-05-21T11:45:00"
  }
]
```

### 4. Criar Nova Tarefa (Autenticado)

**Usando curl (substitua YOUR_JWT_TOKEN pelo token obtido anteriormente):**

```bash
curl -X POST http://localhost:8082/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"title": "Nova Tarefa Segura", "description": "Descrição da nova tarefa segura", "completed": false}'
```

**Resposta esperada:**

```json
{
  "id": 6,
  "title": "Nova Tarefa Segura",
  "description": "Descrição da nova tarefa segura",
  "completed": false,
  "createdAt": "2023-05-20T16:45:30.123",
  "updatedAt": "2023-05-20T16:45:30.123"
}
```

### 5. Registrar Novo Usuário

**Usando curl:**

```bash
curl -X POST http://localhost:8082/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "newuser", "email": "newuser@example.com", "password": "password123", "roles": ["USER"]}'
```

**Resposta esperada:**

```json
{
  "message": "User registered successfully!"
}
```

### 6. Acessar o Console H2

O console H2 está disponível em `http://localhost:8082/api/h2-console` com as seguintes credenciais:
- JDBC URL: jdbc:h2:mem:securedb
- Username: sa
- Password: password

## Testando Batch Low Platform

### 1. Verificar Status do Batch

**Usando curl:**

```bash
curl -X GET http://localhost:8083/api/batch/status
```

**Resposta esperada:**

```json
{
  "status": "UP",
  "service": "Batch Low Platform",
  "timestamp": "2023-05-20T15:30:45.123"
}
```

### 2. Verificar Saúde do Batch

**Usando curl:**

```bash
curl -X GET http://localhost:8083/api/batch/health
```

**Resposta esperada:**

```json
{
  "status": "UP",
  "service": "Batch Low Platform"
}
```

### 3. Iniciar o Job de Batch

**Usando curl:**

```bash
curl -X POST http://localhost:8083/api/batch/start
```

**Resposta esperada:**

```json
{
  "status": "Job started successfully",
  "timestamp": "2023-05-20T15:30:45.123"
}
```

### 4. Acessar o Console H2 do Batch

O console H2 está disponível em `http://localhost:8083/h2-console` com as seguintes credenciais:
- JDBC URL: jdbc:h2:mem:batchdb
- Username: sa
- Password: password

## Testando Endpoints SOAP

### 1. Calculator SOAP Service

**Usando curl:**

```bash
curl -X POST -H "Content-Type: text/xml;charset=UTF-8" -H "SOAPAction: http://tempuri.org/Add" -d @- http://www.dneonline.com/calculator.asmx << EOF
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
   <soapenv:Header/>
   <soapenv:Body>
      <tem:Add>
         <tem:intA>10</tem:intA>
         <tem:intB>20</tem:intB>
      </tem:Add>
   </soapenv:Body>
</soapenv:Envelope>
EOF
```

**Resposta esperada:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <soap:Body>
    <AddResponse xmlns="http://tempuri.org/">
      <AddResult>30</AddResult>
    </AddResponse>
  </soap:Body>
</soap:Envelope>
```

### 2. Temperature Converter SOAP Service

**Usando curl:**

```bash
curl -X POST -H "Content-Type: text/xml;charset=UTF-8" -H "SOAPAction: https://www.w3schools.com/xml/CelsiusToFahrenheit" -d @- https://www.w3schools.com/xml/tempconvert.asmx << EOF
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="https://www.w3schools.com/xml/">
   <soapenv:Header/>
   <soapenv:Body>
      <web:CelsiusToFahrenheit>
         <web:Celsius>25</web:Celsius>
      </web:CelsiusToFahrenheit>
   </soapenv:Body>
</soapenv:Envelope>
EOF
```

**Resposta esperada:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <soap:Body>
    <CelsiusToFahrenheitResponse xmlns="https://www.w3schools.com/xml/">
      <CelsiusToFahrenheitResult>77</CelsiusToFahrenheitResult>
    </CelsiusToFahrenheitResponse>
  </soap:Body>
</soap:Envelope>
```

### 3. Number Conversion SOAP Service

**Usando curl:**

```bash
curl -X POST -H "Content-Type: text/xml;charset=UTF-8" -H "SOAPAction: http://www.dataaccess.com/webservicesserver/NumberToWords" -d @- https://www.dataaccess.com/webservicesserver/NumberConversion.wso << EOF
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://www.dataaccess.com/webservicesserver/">
   <soapenv:Header/>
   <soapenv:Body>
      <web:NumberToWords>
         <web:ubiNum>1984</web:ubiNum>
      </web:NumberToWords>
   </soapenv:Body>
</soapenv:Envelope>
EOF
```

**Resposta esperada:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <m:NumberToWordsResponse xmlns:m="http://www.dataaccess.com/webservicesserver/">
      <m:NumberToWordsResult>one thousand nine hundred and eighty four</m:NumberToWordsResult>
    </m:NumberToWordsResponse>
  </soap:Body>
</soap:Envelope>
```

## Testando com SoapUI

Para testar os serviços SOAP com SoapUI:

1. Baixe e instale o SoapUI (https://www.soapui.org/downloads/soapui/)
2. Crie um novo projeto SOAP
3. Adicione um novo WSDL apontando para um dos URLs WSDL:
   - `http://www.dneonline.com/calculator.asmx?WSDL`
   - `https://www.w3schools.com/xml/tempconvert.asmx?WSDL`
   - `https://www.dataaccess.com/webservicesserver/NumberConversion.wso?WSDL`
4. O SoapUI irá gerar automaticamente as requisições para todas as operações disponíveis
5. Preencha os parâmetros necessários e execute a requisição

## Testando com Postman

Para testar os serviços REST com Postman:

1. Baixe e instale o Postman (https://www.postman.com/downloads/)
2. Crie uma nova requisição para cada endpoint REST:
   - GET `https://viacep.com.br/ws/01001000/json/`
   - GET `https://brasilapi.com.br/api/feriados/v1/2023`
   - GET `https://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL,GBP-BRL`
   - GET `https://api.openweathermap.org/data/2.5/weather?q=Sao%20Paulo,br&appid=YOUR_API_KEY`
   - GET `http://localhost:8081/api/tasks` (API REST não segura)
   - POST `http://localhost:8082/api/auth/login` (API REST segura - autenticação)
   - GET `http://localhost:8082/api/tasks` (API REST segura - com token JWT)
   - GET `http://localhost:8083/api/batch/status` (Batch Low Platform - status)
   - POST `http://localhost:8083/api/batch/start` (Batch Low Platform - iniciar job)
3. Execute as requisições e verifique as respostas

Para testar os serviços SOAP com Postman:

1. Crie uma nova requisição POST para cada endpoint SOAP
2. Defina o header `Content-Type` como `text/xml;charset=UTF-8`
3. Defina o header `SOAPAction` com o valor apropriado
4. No corpo da requisição, adicione o envelope SOAP correspondente
5. Execute as requisições e verifique as respostas