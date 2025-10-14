# Unsecure REST API

This is a simple unsecure REST API built with Spring Boot 3 and Java 21 for testing purposes.

## Features

- CRUD operations for tasks
- Health check endpoint
- Echo endpoint
- H2 in-memory database with pre-loaded test data

## Endpoints

### Tasks

- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create a new task
- `PUT /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task

### Utility

- `GET /api/health` - Health check endpoint
- `GET /api/status` - Detailed status information
- `GET /api/echo` - Echo endpoint (GET)
- `POST /api/echo` - Echo endpoint (POST)
- `PUT /api/echo` - Echo endpoint (PUT)
- `DELETE /api/echo` - Echo endpoint (DELETE)

## H2 Console

The H2 console is available at `/api/h2-console` with the following credentials:
- JDBC URL: jdbc:h2:mem:unsecuredb
- Username: sa
- Password: password