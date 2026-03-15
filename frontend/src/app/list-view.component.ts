import { ChangeDetectionStrategy, Component, DestroyRef, effect, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TodoStoreService } from './todo-store.service';

@Component({
  templateUrl: './list-view.component.html',
  imports: [CommonModule, FormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ListViewComponent {
  protected readonly store = inject(TodoStoreService);
  protected readonly newItemText = signal('');
  protected readonly editingId = signal<number | null>(null);
  protected readonly editingText = signal('');

  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly destroyRef = inject(DestroyRef);
  private selectionRequested = false;

  constructor() {
    const paramSubscription = this.route.params.subscribe((params: Params) => {
      const rawId = params['id'];
      if (!rawId) {
        return;
      }
      const listId = Number(rawId);
      if (!Number.isInteger(listId)) {
        return;
      }
      this.selectionRequested = true;
      this.store.selectList(listId);
      this.cancelEdit();
    });
    this.destroyRef.onDestroy(() => paramSubscription.unsubscribe());

    effect(() => {
      if (!this.selectionRequested) {
        return;
      }
      if (this.store.selectedListId() === null) {
        this.router.navigate(['']);
      }
    });

    effect(() => {
      this.store.selectedListId();
      this.cancelEdit();
    });
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

  protected renameList(): void {
    this.store.updateSelectedListTitle();
  }
}
