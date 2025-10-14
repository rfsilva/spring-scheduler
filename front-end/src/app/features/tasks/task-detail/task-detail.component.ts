import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TaskService } from '../../../core/services/task.service';
import { ScheduledTask, TaskStatus } from '../../../core/models/scheduled-task.model';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-task-detail',
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.scss']
})
export class TaskDetailComponent implements OnInit {
  task: ScheduledTask | null = null;
  loading = true;
  error = false;
  taskId!: number;

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
    this.error = false;
    
    this.taskService.getTaskById(this.taskId).subscribe({
      next: (task) => {
        this.task = task;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading task', err);
        this.error = true;
        this.loading = false;
      }
    });
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
        this.snackBar.open('Task activated successfully', 'Close', { duration: 3000 });
      },
      error: (err) => {
        console.error('Error activating task', err);
        this.snackBar.open('Failed to activate task', 'Close', { duration: 3000 });
      }
    });
  }

  deactivateTask(): void {
    this.taskService.deactivateTask(this.taskId).subscribe({
      next: (updatedTask) => {
        this.task = updatedTask;
        this.snackBar.open('Task deactivated successfully', 'Close', { duration: 3000 });
      },
      error: (err) => {
        console.error('Error deactivating task', err);
        this.snackBar.open('Failed to deactivate task', 'Close', { duration: 3000 });
      }
    });
  }

  deleteTask(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirm Delete',
        message: `Are you sure you want to delete the task "${this.task?.name}"?`,
        confirmButtonText: 'Delete',
        cancelButtonText: 'Cancel'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.taskService.deleteTask(this.taskId).subscribe({
          next: () => {
            this.snackBar.open('Task deleted successfully', 'Close', { duration: 3000 });
            this.router.navigate(['/tasks']);
          },
          error: (err) => {
            console.error('Error deleting task', err);
            this.snackBar.open('Failed to delete task', 'Close', { duration: 3000 });
          }
        });
      }
    });
  }

  getStatusClass(): string {
    return this.task ? `status-${this.task.status.toLowerCase()}` : '';
  }

  goBack(): void {
    this.router.navigate(['/tasks']);
  }
}