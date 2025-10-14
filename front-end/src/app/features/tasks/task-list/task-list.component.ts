import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TaskService } from '../../../core/services/task.service';
import { ScheduledTask, TaskStatus } from '../../../core/models/scheduled-task.model';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent implements OnInit {
  displayedColumns: string[] = ['name', 'taskType', 'status', 'cronExpression', 'actions'];
  dataSource = new MatTableDataSource<ScheduledTask>([]);
  loading = true;
  error = false;

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

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }

  loadTasks(): void {
    this.loading = true;
    this.error = false;
    
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        this.dataSource.data = tasks;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading tasks', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
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
    if (!task.id) return;
    
    this.taskService.activateTask(task.id).subscribe({
      next: (updatedTask) => {
        const index = this.dataSource.data.findIndex(t => t.id === task.id);
        if (index !== -1) {
          const updatedData = [...this.dataSource.data];
          updatedData[index] = updatedTask;
          this.dataSource.data = updatedData;
        }
        this.snackBar.open('Task activated successfully', 'Close', { duration: 3000 });
      },
      error: (err) => {
        console.error('Error activating task', err);
        this.snackBar.open('Failed to activate task', 'Close', { duration: 3000 });
      }
    });
  }

  deactivateTask(task: ScheduledTask): void {
    if (!task.id) return;
    
    this.taskService.deactivateTask(task.id).subscribe({
      next: (updatedTask) => {
        const index = this.dataSource.data.findIndex(t => t.id === task.id);
        if (index !== -1) {
          const updatedData = [...this.dataSource.data];
          updatedData[index] = updatedTask;
          this.dataSource.data = updatedData;
        }
        this.snackBar.open('Task deactivated successfully', 'Close', { duration: 3000 });
      },
      error: (err) => {
        console.error('Error deactivating task', err);
        this.snackBar.open('Failed to deactivate task', 'Close', { duration: 3000 });
      }
    });
  }

  deleteTask(task: ScheduledTask): void {
    if (!task.id) return;
    
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirm Delete',
        message: `Are you sure you want to delete the task "${task.name}"?`,
        confirmButtonText: 'Delete',
        cancelButtonText: 'Cancel'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.taskService.deleteTask(task.id!).subscribe({
          next: () => {
            this.dataSource.data = this.dataSource.data.filter(t => t.id !== task.id);
            this.snackBar.open('Task deleted successfully', 'Close', { duration: 3000 });
          },
          error: (err) => {
            console.error('Error deleting task', err);
            this.snackBar.open('Failed to delete task', 'Close', { duration: 3000 });
          }
        });
      }
    });
  }

  createTask(): void {
    this.router.navigate(['/tasks/new']);
  }

  getStatusClass(status: TaskStatus): string {
    return `status-${status.toLowerCase()}`;
  }
}