# REST API Secure

Este projeto implementa uma API REST segura usando Spring Boot com autenticação JWT (JSON Web Token). Ele serve como um ambiente de teste para o task-scheduler, demonstrando como integrar com APIs que requerem autenticação.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Security
- JWT (JSON Web Token)
- H2 Database (em memória)
- Maven

## Arquitetura de Segurança

### Autenticação JWT

O projeto implementa autenticação baseada em token JWT com os seguintes componentes:

1. **Geração de Token**: Após login bem-sucedido, o servidor gera um token JWT assinado
2. **Validação de Token**: Cada requisição é validada verificando a assinatura e expiração do token
3. **Armazenamento de Credenciais**: Senhas armazenadas com codificação BCrypt
4. **Controle de Acesso**: Baseado em papéis (RBAC - Role-Based Access Control)

### Fluxo de Autenticação

```
Cliente                                  Servidor
   |                                        |
   |  1. POST /auth/register (opcional)     |
   | -------------------------------------> |
   |                                        |
   |  2. POST /auth/login                   |
   |     (username, password)               |
   | -------------------------------------> |
   |                                        |
   |  3. Resposta com JWT Token             |
   | <------------------------------------- |
   |                                        |
   |  4. Requisição com header              |
   |     Authorization: Bearer {token}      |
   | -------------------------------------> |
   |                                        |
   |  5. Resposta do recurso protegido      |
   | <------------------------------------- |
   |                                        |
```

## Endpoints da API

### Endpoints Públicos (Não requerem autenticação)

- **Autenticação**
  - `POST /api/auth/register` - Registrar novo usuário
  - `POST /api/auth/login` - Autenticar e obter token JWT

- **Utilitários**
  - `GET /api/health` - Verificar status da API
  - `GET /api/echo/{message}` - Endpoint de eco para testes

### Endpoints Protegidos (Requerem autenticação)

- **Tarefas**
  - `GET /api/tasks` - Listar todas as tarefas
  - `GET /api/tasks/{id}` - Obter tarefa específica
  - `POST /api/tasks` - Criar nova tarefa
  - `PUT /api/tasks/{id}` - Atualizar tarefa existente
  - `DELETE /api/tasks/{id}` - Excluir tarefa

## Como Usar

### Registro de Usuário

```bash
curl -X POST http://localhost:8082/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "roles": ["USER"]
  }'
```

### Login e Obtenção de Token

```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "roles": ["USER"]
}
```

### Acessando Recursos Protegidos

```bash
curl -X GET http://localhost:8082/api/tasks \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## Configuração

As principais configurações estão no arquivo `application.properties`:

- **Porta do servidor**: 8082
- **Contexto da aplicação**: /api
- **Banco de dados**: H2 em memória
- **Segredo JWT**: Configurado para ambiente de teste
- **Expiração do token**: 24 horas (86400000 ms)

## Estrutura do Projeto

```
src/main/java/com/example/api/secure/
├── RestApiSecureApplication.java       # Classe principal
├── config/                             # Configurações
├── controller/                         # Controladores REST
│   ├── AuthController.java             # Autenticação
│   ├── EchoController.java             # Endpoint de eco
│   ├── StatusController.java           # Status da API
│   └── TaskController.java             # CRUD de tarefas
├── dto/                                # Objetos de transferência de dados
│   ├── JwtResponse.java                # Resposta com token JWT
│   ├── LoginRequest.java               # Requisição de login
│   ├── MessageResponse.java            # Resposta genérica
│   └── SignupRequest.java              # Requisição de registro
├── model/                              # Entidades JPA
│   ├── Task.java                       # Modelo de tarefa
│   └── User.java                       # Modelo de usuário
├── repository/                         # Repositórios Spring Data
│   ├── TaskRepository.java             # Operações de banco para tarefas
│   └── UserRepository.java             # Operações de banco para usuários
└── security/                           # Componentes de segurança
    ├── JwtAuthenticationEntryPoint.java # Tratamento de erro de autenticação
    ├── JwtAuthenticationFilter.java     # Filtro de autenticação JWT
    ├── JwtTokenUtil.java               # Utilitário para tokens JWT
    ├── UserDetailsServiceImpl.java      # Implementação de UserDetailsService
    └── WebSecurityConfig.java           # Configuração de segurança
```

## Integração com Task-Scheduler

Para integrar esta API com o task-scheduler:

1. Configure uma tarefa REST com autenticação do tipo `BEARER_TOKEN`
2. Obtenha um token válido através do endpoint de login
3. Configure o token no campo apropriado da tarefa
4. Defina o endpoint desejado e método HTTP

## Desenvolvimento e Testes

Para executar o projeto localmente:

```bash
./mvnw spring-boot:run
```

O console H2 está disponível em: http://localhost:8082/api/h2-console
- JDBC URL: `jdbc:h2:mem:securedb`
- Username: `sa`
- Password: `password`