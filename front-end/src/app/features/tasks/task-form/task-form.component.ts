import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TaskService } from '../../../core/services/task.service';
import { 
  ScheduledTask, 
  TaskType, 
  TaskStatus, 
  HttpMethod, 
  RestAuthType, 
  SoapAuthType, 
  BrokerType, 
  MessagingAuthType, 
  DeliveryMode 
} from '../../../core/models/scheduled-task.model';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.scss']
})
export class TaskFormComponent implements OnInit {
  taskForm!: FormGroup;
  isEditMode = false;
  taskId?: number;
  loading = false;
  submitting = false;
  
  // Enums for select options
  taskTypes = Object.values(TaskType);
  taskStatuses = Object.values(TaskStatus);
  httpMethods = Object.values(HttpMethod);
  restAuthTypes = Object.values(RestAuthType);
  soapAuthTypes = Object.values(SoapAuthType);
  brokerTypes = Object.values(BrokerType);
  messagingAuthTypes = Object.values(MessagingAuthType);
  deliveryModes = Object.values(DeliveryMode);

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initForm();
    
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.taskId = +id;
        this.isEditMode = true;
        this.loadTask();
      }
    });
    
    // Listen for task type changes to show/hide specific config sections
    this.taskForm.get('taskType')?.valueChanges.subscribe(taskType => {
      this.updateConfigFormVisibility(taskType);
    });
  }

  initForm(): void {
    this.taskForm = this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      taskType: [TaskType.REST, [Validators.required]],
      status: [TaskStatus.INACTIVE, [Validators.required]],
      cronExpression: ['0 0 * * * ?', [Validators.required]],
      maxRetries: [3],
      retryDelaySeconds: [60],
      
      // REST Task Config
      restTaskConfig: this.fb.group({
        url: ['', [Validators.required]],
        method: [HttpMethod.GET, [Validators.required]],
        headers: [''],
        body: [''],
        authType: [RestAuthType.NONE, [Validators.required]],
        username: [''],
        password: [''],
        token: [''],
        apiKeyName: [''],
        apiKeyValue: [''],
        connectTimeoutSeconds: [30],
        readTimeoutSeconds: [30]
      }),
      
      // SOAP Task Config
      soapTaskConfig: this.fb.group({
        endpointUrl: ['', [Validators.required]],
        soapAction: [''],
        requestXml: ['', [Validators.required]],
        authType: [SoapAuthType.NONE, [Validators.required]],
        username: [''],
        password: [''],
        connectTimeoutSeconds: [30],
        readTimeoutSeconds: [30]
      }),
      
      // Low Platform Batch Config
      lowPlatformBatchConfig: this.fb.group({
        command: ['', [Validators.required]],
        workingDirectory: [''],
        environmentVariables: [''],
        successExitCodes: ['0'],
        onSuccessCommand: [''],
        onFailureCommand: [''],
        timeoutSeconds: [300]
      }),
      
      // High Platform Batch Config
      highPlatformBatchConfig: this.fb.group({
        systemType: ['', [Validators.required]],
        jobName: ['', [Validators.required]],
        parameters: [''],
        credentials: [''],
        connectionDetails: [''],
        timeoutSeconds: [300]
      }),
      
      // Messaging Task Config
      messagingTaskConfig: this.fb.group({
        brokerType: [BrokerType.ACTIVEMQ, [Validators.required]],
        destination: ['', [Validators.required]],
        message: ['', [Validators.required]],
        connectionProperties: [''],
        authType: [MessagingAuthType.NONE, [Validators.required]],
        username: [''],
        password: [''],
        deliveryMode: [DeliveryMode.PERSISTENT, [Validators.required]],
        timeoutSeconds: [30]
      })
    });
    
    // Initialize with the default task type
    this.updateConfigFormVisibility(TaskType.REST);
  }

  updateConfigFormVisibility(taskType: TaskType): void {
    // Disable all config forms first
    this.taskForm.get('restTaskConfig')?.disable();
    this.taskForm.get('soapTaskConfig')?.disable();
    this.taskForm.get('lowPlatformBatchConfig')?.disable();
    this.taskForm.get('highPlatformBatchConfig')?.disable();
    this.taskForm.get('messagingTaskConfig')?.disable();
    
    // Enable only the relevant config form
    switch (taskType) {
      case TaskType.REST:
        this.taskForm.get('restTaskConfig')?.enable();
        break;
      case TaskType.SOAP:
        this.taskForm.get('soapTaskConfig')?.enable();
        break;
      case TaskType.LOW_PLATFORM_BATCH:
        this.taskForm.get('lowPlatformBatchConfig')?.enable();
        break;
      case TaskType.HIGH_PLATFORM_BATCH:
        this.taskForm.get('highPlatformBatchConfig')?.enable();
        break;
      case TaskType.MESSAGING:
        this.taskForm.get('messagingTaskConfig')?.enable();
        break;
    }
  }

  loadTask(): void {
    if (!this.taskId) return;
    
    this.loading = true;
    
    this.taskService.getTaskById(this.taskId).subscribe({
      next: (task) => {
        this.updateFormWithTask(task);
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading task', err);
        this.snackBar.open('Failed to load task details', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  updateFormWithTask(task: ScheduledTask): void {
    // Update main task fields
    this.taskForm.patchValue({
      name: task.name,
      description: task.description,
      taskType: task.taskType,
      status: task.status,
      cronExpression: task.cronExpression,
      maxRetries: task.maxRetries,
      retryDelaySeconds: task.retryDelaySeconds
    });
    
    // Update specific config based on task type
    switch (task.taskType) {
      case TaskType.REST:
        if (task.restTaskConfig) {
          this.taskForm.get('restTaskConfig')?.patchValue(task.restTaskConfig);
        }
        break;
      case TaskType.SOAP:
        if (task.soapTaskConfig) {
          this.taskForm.get('soapTaskConfig')?.patchValue(task.soapTaskConfig);
        }
        break;
      case TaskType.LOW_PLATFORM_BATCH:
        if (task.lowPlatformBatchConfig) {
          this.taskForm.get('lowPlatformBatchConfig')?.patchValue(task.lowPlatformBatchConfig);
        }
        break;
      case TaskType.HIGH_PLATFORM_BATCH:
        if (task.highPlatformBatchConfig) {
          this.taskForm.get('highPlatformBatchConfig')?.patchValue(task.highPlatformBatchConfig);
        }
        break;
      case TaskType.MESSAGING:
        if (task.messagingTaskConfig) {
          this.taskForm.get('messagingTaskConfig')?.patchValue(task.messagingTaskConfig);
        }
        break;
    }
    
    // Update form visibility based on task type
    this.updateConfigFormVisibility(task.taskType);
  }

  onSubmit(): void {
    if (this.taskForm.invalid) {
      this.markFormGroupTouched(this.taskForm);
      this.snackBar.open('Please fix the errors in the form', 'Close', { duration: 3000 });
      return;
    }
    
    this.submitting = true;
    
    // Prepare task data
    const taskData = this.prepareTaskData();
    
    if (this.isEditMode && this.taskId) {
      // Update existing task
      this.taskService.updateTask(this.taskId, taskData).subscribe({
        next: (task) => {
          this.snackBar.open('Task updated successfully', 'Close', { duration: 3000 });
          this.router.navigate(['/tasks', task.id]);
          this.submitting = false;
        },
        error: (err) => {
          console.error('Error updating task', err);
          this.snackBar.open('Failed to update task', 'Close', { duration: 3000 });
          this.submitting = false;
        }
      });
    } else {
      // Create new task
      this.taskService.createTask(taskData).subscribe({
        next: (task) => {
          this.snackBar.open('Task created successfully', 'Close', { duration: 3000 });
          this.router.navigate(['/tasks', task.id]);
          this.submitting = false;
        },
        error: (err) => {
          console.error('Error creating task', err);
          this.snackBar.open('Failed to create task', 'Close', { duration: 3000 });
          this.submitting = false;
        }
      });
    }
  }

  prepareTaskData(): ScheduledTask {
    const formValue = this.taskForm.value;
    const taskType = formValue.taskType;
    
    // Create base task object
    const task: ScheduledTask = {
      name: formValue.name,
      description: formValue.description,
      taskType: formValue.taskType,
      status: formValue.status,
      cronExpression: formValue.cronExpression,
      maxRetries: formValue.maxRetries,
      retryDelaySeconds: formValue.retryDelaySeconds
    };
    
    // Add task ID if in edit mode
    if (this.isEditMode && this.taskId) {
      task.id = this.taskId;
    }
    
    // Add specific config based on task type
    switch (taskType) {
      case TaskType.REST:
        task.restTaskConfig = formValue.restTaskConfig;
        break;
      case TaskType.SOAP:
        task.soapTaskConfig = formValue.soapTaskConfig;
        break;
      case TaskType.LOW_PLATFORM_BATCH:
        task.lowPlatformBatchConfig = formValue.lowPlatformBatchConfig;
        break;
      case TaskType.HIGH_PLATFORM_BATCH:
        task.highPlatformBatchConfig = formValue.highPlatformBatchConfig;
        break;
      case TaskType.MESSAGING:
        task.messagingTaskConfig = formValue.messagingTaskConfig;
        break;
    }
    
    return task;
  }

  markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  cancel(): void {
    if (this.isEditMode && this.taskId) {
      this.router.navigate(['/tasks', this.taskId]);
    } else {
      this.router.navigate(['/tasks']);
    }
  }
}