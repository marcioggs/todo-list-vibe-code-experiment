import { Injectable, inject, signal } from '@angular/core';
import { finalize } from 'rxjs';
import { TodoApiService } from './todo-api.service';
import { TodoItem } from './todo-item.model';

@Injectable({ providedIn: 'root' })
export class TodoStoreService {
  private readonly todoApiService = inject(TodoApiService);

  readonly items = signal<TodoItem[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .findAll()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (items) => this.items.set(items),
        error: () => this.error.set('Could not load TODO items.')
      });
  }

  add(text: string): void {
    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .create(text)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (item) => this.items.update((items) => [...items, item]),
        error: () => this.error.set('Could not add the TODO item.')
      });
  }

  update(id: number, text: string): void {
    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .update(id, text)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (updatedItem) =>
          this.items.update((items) =>
            items.map((item) => (item.id === updatedItem.id ? updatedItem : item))
          ),
        error: () => this.error.set('Could not update the TODO item.')
      });
  }

  remove(id: number): void {
    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .remove(id)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => this.items.update((items) => items.filter((item) => item.id !== id)),
        error: () => this.error.set('Could not remove the TODO item.')
      });
  }
}
