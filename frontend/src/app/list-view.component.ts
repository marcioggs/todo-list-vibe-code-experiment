import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  inject,
  linkedSignal,
  signal
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Params } from '@angular/router';
import { TodoStoreService } from './todo-store.service';

@Component({
  templateUrl: './list-view.component.html',
  imports: [CommonModule, FormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ListViewComponent {
  protected readonly store = inject(TodoStoreService);
  protected readonly newItemText = signal('');
  protected readonly editingId = linkedSignal<number | null>(() => {
    this.store.selectedListId();
    return null;
  });
  protected readonly editingText = linkedSignal(() => {
    this.store.selectedListId();
    return '';
  });
  private readonly route = inject(ActivatedRoute);
  private readonly destroyRef = inject(DestroyRef);

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
      this.store.selectList(listId);
      this.cancelEdit();
    });
    this.destroyRef.onDestroy(() => paramSubscription.unsubscribe());

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
