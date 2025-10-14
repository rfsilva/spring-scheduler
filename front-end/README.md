# Task Scheduler Frontend

Este é o frontend para a aplicação Task Scheduler, desenvolvido com Angular 18 e Angular Material.

## Requisitos

- Node.js 18+
- npm 9+

## Instalação

```bash
npm install
```

## Desenvolvimento

Para iniciar o servidor de desenvolvimento:

```bash
npm start
```

A aplicação estará disponível em `http://localhost:4200/`.

## Build

Para gerar uma build de produção:

```bash
npm run build
```

Os arquivos de build serão gerados na pasta `dist/`.

## Docker

Para construir a imagem Docker:

```bash
docker build -t task-scheduler-frontend .
```

Para executar o container:

```bash
docker run -p 80:80 task-scheduler-frontend
```

## Estrutura do Projeto

- `src/app/core`: Serviços, modelos e interceptors principais
- `src/app/features`: Componentes de funcionalidades (dashboard, tarefas, execuções)
- `src/app/layout`: Componentes de layout (header, sidenav)
- `src/app/shared`: Componentes compartilhados

## Funcionalidades

- Dashboard com visão geral das tarefas
- Listagem de tarefas agendadas
- Criação e edição de tarefas
- Visualização de detalhes das tarefas
- Histórico de execuções de tarefas
- Ativação/desativação de tarefas