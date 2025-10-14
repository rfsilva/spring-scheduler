import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { TaskExecutionService } from '../../core/services/task-execution.service';
import { TaskService } from '../../core/services/task.service';
import { TaskExecution } from '../../core/models/task-execution.model';
import { ScheduledTask, TaskStatus } from '../../core/models/scheduled-task.model';
import { format } from 'date-fns';

@Component({
  selector: 'app-task-executions',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatExpansionModule,
    MatChipsModule,
    MatTooltipModule,
    MatDialogModule
  ],
  templateUrl: './task-executions.component.html',
  styleUrls: ['./task-executions.component.scss']
})
export class TaskExecutionsComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['id', 'startTime', 'endTime', 'status', 'durationMs', 'retryCount', 'actions'];
  dataSource = new MatTableDataSource<TaskExecution>([]);
  task?: ScheduledTask;
  taskId!: number;
  loading = true;
  showDateFilter = false;
  dateFilterForm: FormGroup;
  selectedExecution?: TaskExecution;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private taskExecutionService: TaskExecutionService,
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.dateFilterForm = this.fb.group({
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

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadTask(): void {
    this.taskService.getTaskById(this.taskId).subscribe({
      next: (task) => {
        this.task = task;
      }
    });
  }

  loadExecutions(): void {
    this.loading = true;
    this.taskExecutionService.getTaskExecutionHistory(this.taskId).subscribe({
      next: (executions) => {
        this.dataSource.data = executions;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  toggleDateFilter(): void {
    this.showDateFilter = !this.showDateFilter;
  }

  applyDateFilter(): void {
    const startDate = this.dateFilterForm.get('startDate')?.value;
    const endDate = this.dateFilterForm.get('endDate')?.value;
    
    if (startDate && endDate) {
      const startIso = format(startDate, "yyyy-MM-dd'T'00:00:00");
      const endIso = format(endDate, "yyyy-MM-dd'T'23:59:59");
      
      this.loading = true;
      this.taskExecutionService.getTaskExecutionHistoryByDateRange(this.taskId, startIso, endIso).subscribe({
        next: (executions) => {
          this.dataSource.data = executions;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });
    }
  }

  resetDateFilter(): void {
    this.dateFilterForm.reset();
    this.loadExecutions();
  }

  goBack(): void {
    this.router.navigate(['/tasks', this.taskId]);
  }

  viewExecutionDetails(execution: TaskExecution): void {
    this.selectedExecution = execution;
  }

  getStatusClass(status: string): string {
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