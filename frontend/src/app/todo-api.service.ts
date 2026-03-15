import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TodoItem } from './todo-item.model';
import { TodoList } from './todo-list.model';

@Injectable({ providedIn: 'root' })
export class TodoApiService {
  private readonly http = inject(HttpClient);

  findAllLists(): Observable<TodoList[]> {
    return this.http.get<TodoList[]>('/api/lists');
  }

  createList(title: string): Observable<TodoList> {
    return this.http.post<TodoList>('/api/lists', { title });
  }

  updateList(id: number, title: string): Observable<TodoList> {
    return this.http.put<TodoList>(`/api/lists/${id}`, { title });
  }

  deleteList(id: number): Observable<void> {
    return this.http.delete<void>(`/api/lists/${id}`);
  }

  createItem(listId: number, text: string): Observable<TodoItem> {
    return this.http.post<TodoItem>(`/api/lists/${listId}/todos`, { text });
  }

  updateItem(listId: number, id: number, text: string): Observable<TodoItem> {
    return this.http.put<TodoItem>(`/api/lists/${listId}/todos/${id}`, { text });
  }

  deleteItem(listId: number, id: number): Observable<void> {
    return this.http.delete<void>(`/api/lists/${listId}/todos/${id}`);
  }
}
