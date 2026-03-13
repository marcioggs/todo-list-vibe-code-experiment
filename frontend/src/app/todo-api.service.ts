import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TodoItem } from './todo-item.model';

@Injectable({ providedIn: 'root' })
export class TodoApiService {
  private readonly http = inject(HttpClient);

  findAll(): Observable<TodoItem[]> {
    return this.http.get<TodoItem[]>('/api/todos');
  }

  create(text: string): Observable<TodoItem> {
    return this.http.post<TodoItem>('/api/todos', { text });
  }

  update(id: number, text: string): Observable<TodoItem> {
    return this.http.put<TodoItem>(`/api/todos/${id}`, { text });
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`/api/todos/${id}`);
  }
}
