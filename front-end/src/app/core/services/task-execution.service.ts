import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskExecution } from '../models/task-execution.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskExecutionService {
  private apiUrl = `${environment.apiUrl}/tasks`;

  constructor(private http: HttpClient) { }

  getTaskExecutionHistory(taskId: number): Observable<TaskExecution[]> {
    return this.http.get<TaskExecution[]>(`${this.apiUrl}/${taskId}/executions`);
  }

  getTaskExecutionHistoryPaged(taskId: number, page: number, size: number, sort?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    if (sort) {
      params = params.set('sort', sort);
    }
    
    return this.http.get<any>(`${this.apiUrl}/${taskId}/executions/paged`, { params });
  }

  getTaskExecutionHistoryByDateRange(taskId: number, start: string, end: string): Observable<TaskExecution[]> {
    let params = new HttpParams()
      .set('start', start)
      .set('end', end);
    
    return this.http.get<TaskExecution[]>(`${this.apiUrl}/${taskId}/executions/date-range`, { params });
  }
}