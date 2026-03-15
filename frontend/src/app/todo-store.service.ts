import { Injectable, computed, inject, signal } from '@angular/core';
import { finalize } from 'rxjs';
import { TodoApiService } from './todo-api.service';
import { TodoItem } from './todo-item.model';
import { TodoList } from './todo-list.model';

@Injectable({ providedIn: 'root' })
export class TodoStoreService {
  private readonly todoApiService = inject(TodoApiService);

  readonly lists = signal<TodoList[]>([]);
  readonly selectedListId = signal<number | null>(null);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly selectedListTitle = signal('');
  readonly lastCreatedListId = signal<number | null>(null);

  readonly selectedList = computed(() =>
    this.lists().find((list) => list.id === this.selectedListId()) ?? null
  );

  readonly selectedItems = computed(() => this.selectedList()?.items ?? []);

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .findAllLists()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (lists) => {
          this.lists.set(lists);
          if (!lists.length) {
            this.selectedListId.set(null);
            this.selectedListTitle.set('');
            return;
          }
          const current = this.selectedListId();
          if (current !== null) {
            this.selectList(current, lists);
          }
        },
        error: () => this.error.set('Could not load TODO lists.')
      });
  }

  selectList(id: number | null, lists = this.lists()): void {
    this.selectedListId.set(id);
    const next = lists.find((list) => list.id === id);
    this.selectedListTitle.set(next?.title ?? '');
  }

  createList(title: string): void {
    const trimmedTitle = title.trim();
    if (!trimmedTitle) {
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .createList(trimmedTitle)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (list) => {
          this.lists.update((lists) => [...lists, list]);
          this.lastCreatedListId.set(list.id);
          this.selectList(list.id);
        },
        error: () => this.error.set('Could not create the list.')
      });
  }

  updateSelectedListTitle(): void {
    const list = this.selectedList();
    const title = this.selectedListTitle().trim();
    if (!list || !title || title === list.title) {
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .updateList(list.id, title)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (updated) => {
          this.updateListState(updated);
        },
        error: () => this.error.set('Could not update the list title.')
      });
  }

  deleteList(id: number): void {
    this.loading.set(true);
    this.error.set('');
    const wasSelected = this.selectedListId() === id;
    this.todoApiService
      .deleteList(id)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
          next: () => {
            this.lists.update((lists) => lists.filter((list) => list.id !== id));
            if (wasSelected) {
              this.selectedListId.set(null);
              this.selectedListTitle.set('');
            }
          },
        error: () => this.error.set('Could not delete the list.')
      });
  }

  add(text: string): void {
    const list = this.selectedList();
    if (!list) {
      return;
    }
    const trimmed = text.trim();
    if (!trimmed) {
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .createItem(list.id, trimmed)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (item) => {
          this.replaceListItems(list.id, [...list.items, item]);
        },
        error: () => this.error.set('Could not add the TODO item.')
      });
  }

  update(id: number, text: string): void {
    const list = this.selectedList();
    if (!list) {
      return;
    }
    const trimmed = text.trim();
    if (!trimmed) {
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .updateItem(list.id, id, trimmed)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (updatedItem) => {
          this.replaceListItems(
            list.id,
            list.items.map((item) => (item.id === updatedItem.id ? updatedItem : item))
          );
        },
        error: () => this.error.set('Could not update the TODO item.')
      });
  }

  remove(id: number): void {
    const list = this.selectedList();
    if (!list) {
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.todoApiService
      .deleteItem(list.id, id)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.replaceListItems(list.id, list.items.filter((item) => item.id !== id));
        },
        error: () => this.error.set('Could not remove the TODO item.')
      });
  }

  resetLastCreatedListId(): void {
    this.lastCreatedListId.set(null);
  }

  private updateListState(updated: TodoList): void {
    this.lists.update((lists) =>
      lists.map((list) => (list.id === updated.id ? updated : list))
    );
    if (this.selectedListId() === updated.id) {
      this.selectedListTitle.set(updated.title);
    }
  }

  private replaceListItems(listId: number, items: TodoItem[]): void {
    this.lists.update((lists) =>
      lists.map((list) => (list.id === listId ? { ...list, items } : list))
    );
  }
}
