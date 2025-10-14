# Task Scheduler Frontend

This is the frontend application for the Task Scheduler system, built with Angular 18.

## Prerequisites

- Node.js (v16 or higher)
- npm (v8 or higher)

## Installation

```bash
npm install
```

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Docker

To build and run the application using Docker:

```bash
# Build the Docker image
docker build -t task-scheduler-frontend .

# Run the container
docker run -p 80:80 task-scheduler-frontend
```

## Features

- Dashboard with task statistics
- Task management (create, read, update, delete)
- Task activation/deactivation
- Task execution history
- Filtering and sorting capabilities

## Project Structure

- `src/app/core`: Core functionality (models, services, interceptors)
- `src/app/shared`: Shared components and utilities
- `src/app/features`: Feature modules (dashboard, tasks, executions)
- `src/app/layout`: Layout components (header, sidenav)

## OAuth2 Integration

OAuth2 authentication is prepared but currently disabled. To enable it:

1. Uncomment the OAuth2 related code in `src/app/core/interceptors/api.interceptor.ts`
2. Uncomment the user menu in `src/app/layout/header/header.component.html`
3. Implement an authentication service