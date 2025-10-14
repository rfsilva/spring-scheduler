import { TaskStatus } from './scheduled-task.model';

export interface TaskExecution {
  id?: number;
  taskId: number;
  taskName: string;
  startTime: string;
  endTime?: string;
  status: TaskStatus;
  executionMessage?: string;
  executionDetails?: string;
  retryCount?: number;
  durationMs?: number;
}