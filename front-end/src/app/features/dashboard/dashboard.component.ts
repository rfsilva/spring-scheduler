import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TaskService } from '../../core/services/task.service';
import { ScheduledTask, TaskStatus } from '../../core/models/scheduled-task.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  tasks: ScheduledTask[] = [];
  loading = true;
  statusCounts = {
    total: 0,
    active: 0,
    inactive: 0,
    executing: 0,
    completed: 0,
    failed: 0
  };

  constructor(
    private taskService: TaskService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.loading = true;
    this.taskService.getAllTasks().subscribe({
      next: (data) => {
        this.tasks = data;
        this.calculateStatusCounts();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  calculateStatusCounts(): void {
    this.statusCounts.total = this.tasks.length;
    this.statusCounts.active = this.tasks.filter(task => task.status === TaskStatus.ACTIVE).length;
    this.statusCounts.inactive = this.tasks.filter(task => task.status === TaskStatus.INACTIVE).length;
    this.statusCounts.executing = this.tasks.filter(task => task.status === TaskStatus.EXECUTING).length;
    this.statusCounts.completed = this.tasks.filter(task => task.status === TaskStatus.COMPLETED).length;
    this.statusCounts.failed = this.tasks.filter(task => task.status === TaskStatus.FAILED).length;
  }

  navigateToTasks(): void {
    this.router.navigate(['/tasks']);
  }

  navigateToCreateTask(): void {
    this.router.navigate(['/tasks/new']);
  }
}