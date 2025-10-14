import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { TaskService } from '../../../core/services/task.service';
import { ScheduledTask, TaskStatus, TaskType } from '../../../core/models/scheduled-task.model';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatSnackBarModule,
    MatChipsModule
  ],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['id', 'name', 'taskType', 'status', 'cronExpression', 'actions'];
  dataSource = new MatTableDataSource<ScheduledTask>([]);
  loading = true;
  TaskType = TaskType;
  TaskStatus = TaskStatus;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private taskService: TaskService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadTasks(): void {
    this.loading = true;
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        this.dataSource.data = tasks;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  createTask(): void {
    this.router.navigate(['/tasks/new']);
  }

  viewTask(id: number): void {
    this.router.navigate(['/tasks', id]);
  }

  editTask(id: number): void {
    this.router.navigate(['/tasks', id, 'edit']);
  }

  viewExecutions(id: number): void {
    this.router.navigate(['/tasks', id, 'executions']);
  }

  activateTask(task: ScheduledTask): void {
    this.taskService.activateTask(task.id!).subscribe({
      next: (updatedTask) => {
        const index = this.dataSource.data.findIndex(t => t.id === task.id);
        if (index !== -1) {
          this.dataSource.data[index] = updatedTask;
          this.dataSource._updateChangeSubscription();
        }
        this.snackBar.open('Tarefa ativada com sucesso', 'Fechar', { duration: 3000 });
      }
    });
  }

  deactivateTask(task: ScheduledTask): void {
    this.taskService.deactivateTask(task.id!).subscribe({
      next: (updatedTask) => {
        const index = this.dataSource.data.findIndex(t => t.id === task.id);
        if (index !== -1) {
          this.dataSource.data[index] = updatedTask;
          this.dataSource._updateChangeSubscription();
        }
        this.snackBar.open('Tarefa desativada com sucesso', 'Fechar', { duration: 3000 });
      }
    });
  }

  deleteTask(task: ScheduledTask): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmar exclusão',
        message: `Tem certeza que deseja excluir a tarefa "${task.name}"?`,
        confirmButtonText: 'Excluir',
        cancelButtonText: 'Cancelar'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.taskService.deleteTask(task.id!).subscribe({
          next: () => {
            this.dataSource.data = this.dataSource.data.filter(t => t.id !== task.id);
            this.snackBar.open('Tarefa excluída com sucesso', 'Fechar', { duration: 3000 });
          }
        });
      }
    });
  }

  getStatusClass(status: TaskStatus): string {
    switch (status) {
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