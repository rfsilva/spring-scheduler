import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { TaskService } from '../../../core/services/task.service';
import { ScheduledTask, TaskStatus, EndpointType } from '../../../core/models/scheduled-task.model';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatSnackBarModule,
    MatChipsModule
  ],
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.scss']
})
export class TaskDetailComponent implements OnInit {
  task?: ScheduledTask;
  loading = true;
  taskId!: number;
  EndpointType = EndpointType; // Expor o enum para o template

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.taskId = +id;
        this.loadTask();
      }
    });
  }

  loadTask(): void {
    this.loading = true;
    this.taskService.getTaskById(this.taskId).subscribe({
      next: (task) => {
        this.task = task;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/tasks']);
  }

  editTask(): void {
    this.router.navigate(['/tasks', this.taskId, 'edit']);
  }

  viewExecutions(): void {
    this.router.navigate(['/tasks', this.taskId, 'executions']);
  }

  activateTask(): void {
    this.taskService.activateTask(this.taskId).subscribe({
      next: (updatedTask) => {
        this.task = updatedTask;
        this.snackBar.open('Tarefa ativada com sucesso', 'Fechar', { duration: 3000 });
      }
    });
  }

  deactivateTask(): void {
    this.taskService.deactivateTask(this.taskId).subscribe({
      next: (updatedTask) => {
        this.task = updatedTask;
        this.snackBar.open('Tarefa desativada com sucesso', 'Fechar', { duration: 3000 });
      }
    });
  }

  deleteTask(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmar exclusão',
        message: `Tem certeza que deseja excluir a tarefa "${this.task?.name}"?`,
        confirmButtonText: 'Excluir',
        cancelButtonText: 'Cancelar'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.taskService.deleteTask(this.taskId).subscribe({
          next: () => {
            this.snackBar.open('Tarefa excluída com sucesso', 'Fechar', { duration: 3000 });
            this.router.navigate(['/tasks']);
          }
        });
      }
    });
  }

  getStatusClass(): string {
    if (!this.task) return '';
    
    switch (this.task.status) {
      case TaskStatus.ACTIVE:
        return 'status-active';
      case TaskStatus.INACTIVE:
        return 'status-inactive';
      case TaskStatus.EXECUTING:
        return 'status-executing';
      case TaskStatus.COMPLETED:
        return 'status-completed';
      case TaskStatus.FAILED:
        return 'status-failed';
      default:
        return '';
    }
  }
}