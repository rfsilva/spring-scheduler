import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { FormGroup, FormBuilder } from '@angular/forms';
import { TaskExecutionService } from '../../core/services/task-execution.service';
import { TaskService } from '../../core/services/task.service';
import { TaskExecution } from '../../core/models/task-execution.model';
import { ScheduledTask } from '../../core/models/scheduled-task.model';

@Component({
  selector: 'app-task-executions',
  templateUrl: './task-executions.component.html',
  styleUrls: ['./task-executions.component.scss']
})
export class TaskExecutionsComponent implements OnInit {
  displayedColumns: string[] = ['id', 'startTime', 'endTime', 'status', 'durationMs', 'retryCount', 'actions'];
  dataSource = new MatTableDataSource<TaskExecution>([]);
  task: ScheduledTask | null = null;
  loading = true;
  error = false;
  taskId!: number;
  totalElements = 0;
  pageSize = 10;
  pageSizeOptions = [5, 10, 25, 50];
  dateRangeForm: FormGroup;
  showDateFilter = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private taskExecutionService: TaskExecutionService,
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.dateRangeForm = this.fb.group({
      startDate: [''],
      endDate: ['']
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.taskId = +id;
        this.loadTask();
        this.loadExecutions();
      }
    });
  }

  ngAfterViewInit() {
    this.paginator.page.subscribe(() => {
      this.loadExecutions();
    });
    
    this.sort.sortChange.subscribe(() => {
      this.paginator.pageIndex = 0;
      this.loadExecutions();
    });
  }

  loadTask(): void {
    this.taskService.getTaskById(this.taskId).subscribe({
      next: (task) => {
        this.task = task;
      },
      error: (err) => {
        console.error('Error loading task', err);
      }
    });
  }

  loadExecutions(): void {
    this.loading = true;
    this.error = false;
    
    const page = this.paginator ? this.paginator.pageIndex : 0;
    const size = this.paginator ? this.paginator.pageSize : this.pageSize;
    let sort = '';
    
    if (this.sort && this.sort.active && this.sort.direction) {
      sort = `${this.sort.active},${this.sort.direction}`;
    }
    
    this.taskExecutionService.getTaskExecutionHistoryPaged(this.taskId, page, size, sort).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading executions', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  applyDateFilter(): void {
    const startDate = this.dateRangeForm.get('startDate')?.value;
    const endDate = this.dateRangeForm.get('endDate')?.value;
    
    if (startDate && endDate) {
      this.loading = true;
      this.error = false;
      
      const start = new Date(startDate).toISOString();
      const end = new Date(endDate).toISOString();
      
      this.taskExecutionService.getTaskExecutionHistoryByDateRange(this.taskId, start, end).subscribe({
        next: (executions) => {
          this.dataSource.data = executions;
          this.loading = false;
        },
        error: (err) => {
          console.error('Error loading executions by date range', err);
          this.error = true;
          this.loading = false;
        }
      });
    }
  }

  resetDateFilter(): void {
    this.dateRangeForm.reset();
    this.loadExecutions();
  }

  toggleDateFilter(): void {
    this.showDateFilter = !this.showDateFilter;
    if (!this.showDateFilter) {
      this.resetDateFilter();
    }
  }

  viewExecutionDetails(execution: TaskExecution): void {
    // In a real application, you might want to show execution details in a dialog or a separate page
    console.log('View execution details', execution);
  }

  getStatusClass(status: string): string {
    return `status-${status.toLowerCase()}`;
  }

  goBack(): void {
    this.router.navigate(['/tasks', this.taskId]);
  }
}