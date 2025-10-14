import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
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
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDividerModule,
    MatExpansionModule,
    MatSlideToggleModule,
    MatTooltipModule
  ],
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
  TaskType = TaskType;
  TaskStatus = TaskStatus;
  HttpMethod = HttpMethod;
  RestAuthType = RestAuthType;
  SoapAuthType = SoapAuthType;
  BrokerType = BrokerType;
  MessagingAuthType = MessagingAuthType;
  DeliveryMode = DeliveryMode;
  
  // Convert enums to arrays for template
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
        this.isEditMode = true;
        this.taskId = +id;
        this.loadTask();
      }
    });
    
    // Listen for task type changes to show/hide relevant config sections
    this.taskForm.get('taskType')?.valueChanges.subscribe(taskType => {
      this.updateConfigFormVisibility(taskType);
    });
  }

  initForm(): void {
    this.taskForm = this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      taskType: [TaskType.REST_CALL, [Validators.required]],
      status: [TaskStatus.INACTIVE, [Validators.required]],
      cronExpression: ['0 0 * * * ?', [Validators.required]],
      maxRetries: [0],
      retryDelaySeconds: [0],
      
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
        brokerType: [BrokerType.KAFKA, [Validators.required]],
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
    
    // Initialize with REST_CALL selected
    this.updateConfigFormVisibility(TaskType.REST_CALL);
  }

  loadTask(): void {
    this.loading = true;
    this.taskService.getTaskById(this.taskId!).subscribe({
      next: (task) => {
        this.updateFormWithTask(task);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Erro ao carregar a tarefa', 'Fechar', { duration: 3000 });
      }
    });
  }

  updateFormWithTask(task: ScheduledTask): void {
    // Update main form fields
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
      case TaskType.REST_CALL:
        if (task.restTaskConfig) {
          this.taskForm.get('restTaskConfig')?.patchValue(task.restTaskConfig);
        }
        break;
      case TaskType.SOAP_CALL:
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

  updateConfigFormVisibility(taskType: TaskType): void {
    // Reset all config forms
    const restConfig = this.taskForm.get('restTaskConfig');
    const soapConfig = this.taskForm.get('soapTaskConfig');
    const lowPlatformConfig = this.taskForm.get('lowPlatformBatchConfig');
    const highPlatformConfig = this.taskForm.get('highPlatformBatchConfig');
    const messagingConfig = this.taskForm.get('messagingTaskConfig');
    
    // Disable all config forms first
    restConfig?.disable();
    soapConfig?.disable();
    lowPlatformConfig?.disable();
    highPlatformConfig?.disable();
    messagingConfig?.disable();
    
    // Enable only the relevant config form
    switch (taskType) {
      case TaskType.REST_CALL:
        restConfig?.enable();
        break;
      case TaskType.SOAP_CALL:
        soapConfig?.enable();
        break;
      case TaskType.LOW_PLATFORM_BATCH:
        lowPlatformConfig?.enable();
        break;
      case TaskType.HIGH_PLATFORM_BATCH:
        highPlatformConfig?.enable();
        break;
      case TaskType.MESSAGING:
        messagingConfig?.enable();
        break;
    }
  }

  onSubmit(): void {
    if (this.taskForm.invalid) {
      this.markFormGroupTouched(this.taskForm);
      this.snackBar.open('Por favor, corrija os erros no formulário', 'Fechar', { duration: 3000 });
      return;
    }
    
    this.submitting = true;
    const taskData = this.prepareTaskData();
    
    if (this.isEditMode) {
      this.taskService.updateTask(this.taskId!, taskData).subscribe({
        next: () => {
          this.submitting = false;
          this.snackBar.open('Tarefa atualizada com sucesso', 'Fechar', { duration: 3000 });
          this.router.navigate(['/tasks', this.taskId]);
        },
        error: () => {
          this.submitting = false;
        }
      });
    } else {
      this.taskService.createTask(taskData).subscribe({
        next: (createdTask) => {
          this.submitting = false;
          this.snackBar.open('Tarefa criada com sucesso', 'Fechar', { duration: 3000 });
          this.router.navigate(['/tasks', createdTask.id]);
        },
        error: () => {
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
    
    // Add ID if in edit mode
    if (this.isEditMode && this.taskId) {
      task.id = this.taskId;
    }
    
    // Add specific config based on task type
    switch (taskType) {
      case TaskType.REST_CALL:
        task.restTaskConfig = formValue.restTaskConfig;
        break;
      case TaskType.SOAP_CALL:
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