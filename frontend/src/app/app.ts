import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TodoStoreService } from './todo-store.service';

@Component({
  selector: 'app-root',
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly store = inject(TodoStoreService);
  protected readonly newItemText = signal('');
  protected readonly editingId = signal<number | null>(null);
  protected readonly editingText = signal('');
  protected readonly newListTitle = signal('');

  constructor() {
    this.store.load();
  }

  protected createList(): void {
    const title = this.newListTitle().trim();
    if (!title) {
      return;
    }

    this.store.createList(title);
    this.newListTitle.set('');
  }

  protected renameList(): void {
    this.store.updateSelectedListTitle();
  }

  protected selectList(id: number): void {
    this.store.selectList(id);
    this.cancelEdit();
  }

  protected deleteList(id: number): void {
    this.store.deleteList(id);
    this.cancelEdit();
  }

  protected addItem(): void {
    const text = this.newItemText().trim();
    if (!text) {
      return;
    }

    this.store.add(text);
    this.newItemText.set('');
  }

  protected startEdit(id: number, text: string): void {
    this.editingId.set(id);
    this.editingText.set(text);
  }

  protected saveEdit(): void {
    const id = this.editingId();
    const text = this.editingText().trim();
    if (id === null || !text) {
      return;
    }

    this.store.update(id, text);
    this.cancelEdit();
  }

  protected cancelEdit(): void {
    this.editingId.set(null);
    this.editingText.set('');
  }

  protected removeItem(id: number): void {
    this.store.remove(id);
  }
}
