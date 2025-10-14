import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../core/services/task.service';
import { ScheduledTask, TaskStatus } from '../../core/models/scheduled-task.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  tasks: ScheduledTask[] = [];
  loading = true;
  error = false;
  
  taskStatusCounts = {
    active: 0,
    inactive: 0,
    running: 0,
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
    this.error = false;
    
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.calculateStatusCounts();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading tasks', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  calculateStatusCounts(): void {
    // Reset counts
    this.taskStatusCounts = {
      active: 0,
      inactive: 0,
      running: 0,
      completed: 0,
      failed: 0
    };
    
    // Count tasks by status
    this.tasks.forEach(task => {
      switch (task.status) {
        case TaskStatus.ACTIVE:
          this.taskStatusCounts.active++;
          break;
        case TaskStatus.INACTIVE:
          this.taskStatusCounts.inactive++;
          break;
        case TaskStatus.RUNNING:
          this.taskStatusCounts.running++;
          break;
        case TaskStatus.COMPLETED:
          this.taskStatusCounts.completed++;
          break;
        case TaskStatus.FAILED:
          this.taskStatusCounts.failed++;
          break;
      }
    });
  }

  navigateToTasks(): void {
    this.router.navigate(['/tasks']);
  }

  navigateToCreateTask(): void {
    this.router.navigate(['/tasks/new']);
  }
}