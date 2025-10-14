import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ScheduledTask } from '../models/scheduled-task.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = `${environment.apiUrl}/tasks`;

  constructor(private http: HttpClient) { }

  getAllTasks(): Observable<ScheduledTask[]> {
    return this.http.get<ScheduledTask[]>(this.apiUrl);
  }

  getTaskById(id: number): Observable<ScheduledTask> {
    return this.http.get<ScheduledTask>(`${this.apiUrl}/${id}`);
  }

  createTask(task: ScheduledTask): Observable<ScheduledTask> {
    return this.http.post<ScheduledTask>(this.apiUrl, task);
  }

  updateTask(id: number, task: ScheduledTask): Observable<ScheduledTask> {
    return this.http.put<ScheduledTask>(`${this.apiUrl}/${id}`, task);
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  activateTask(id: number): Observable<ScheduledTask> {
    return this.http.post<ScheduledTask>(`${this.apiUrl}/${id}/activate`, {});
  }

  deactivateTask(id: number): Observable<ScheduledTask> {
    return this.http.post<ScheduledTask>(`${this.apiUrl}/${id}/deactivate`, {});
  }
}