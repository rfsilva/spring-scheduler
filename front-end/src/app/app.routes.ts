import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
    title: 'Dashboard - Task Scheduler'
  },
  {
    path: 'tasks',
    loadComponent: () => import('./features/tasks/task-list/task-list.component').then(m => m.TaskListComponent),
    title: 'Tarefas - Task Scheduler'
  },
  {
    path: 'tasks/new',
    loadComponent: () => import('./features/tasks/task-form/task-form.component').then(m => m.TaskFormComponent),
    title: 'Nova Tarefa - Task Scheduler'
  },
  {
    path: 'tasks/:id',
    loadComponent: () => import('./features/tasks/task-detail/task-detail.component').then(m => m.TaskDetailComponent),
    title: 'Detalhes da Tarefa - Task Scheduler'
  },
  {
    path: 'tasks/:id/edit',
    loadComponent: () => import('./features/tasks/task-form/task-form.component').then(m => m.TaskFormComponent),
    title: 'Editar Tarefa - Task Scheduler'
  },
  {
    path: 'tasks/:id/executions',
    loadComponent: () => import('./features/task-executions/task-executions.component').then(m => m.TaskExecutionsComponent),
    title: 'Execuções da Tarefa - Task Scheduler'
  },
  {
    path: '**',
    loadComponent: () => import('./shared/components/page-not-found/page-not-found.component').then(m => m.PageNotFoundComponent),
    title: 'Página não encontrada - Task Scheduler'
  }
];