# Secure REST API

This is a simple secure REST API built with Spring Boot 3, Java 21, and JWT authentication for testing purposes.

## Features

- JWT Authentication
- Role-based authorization
- CRUD operations for tasks
- Health check endpoint
- Echo endpoint
- H2 in-memory database with pre-loaded test data

## Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Tasks

- `GET /api/tasks` - Get all tasks (requires authentication)
- `GET /api/tasks/{id}` - Get task by ID (requires authentication)
- `POST /api/tasks` - Create a new task (requires authentication)
- `PUT /api/tasks/{id}` - Update a task (requires authentication)
- `DELETE /api/tasks/{id}` - Delete a task (requires ADMIN role)

### Utility

- `GET /api/health` - Health check endpoint (public)
- `GET /api/echo/{message}` - Echo endpoint (public)

## Authentication

To access protected endpoints, include the JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

## Default Users

The application creates two default users on startup:

1. Regular User:
   - Username: user
   - Password: password
   - Role: USER

2. Admin User:
   - Username: admin
   - Password: admin
   - Role: ADMIN

## H2 Console

The H2 console is available at `/api/h2-console` with the following credentials:
- JDBC URL: jdbc:h2:mem:securedb
- Username: sa
- Password: password