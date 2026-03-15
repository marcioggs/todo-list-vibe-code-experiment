import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TodoStoreService } from './todo-store.service';

@Component({
  template: `
    @if (store.lists().length) {
      <p class="message empty-hint">Select a list to view its items.</p>
    } @else {
      <p class="message empty-hint">Create a list to get started.</p>
    }
  `,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ListPlaceholderComponent {
  protected readonly store = inject(TodoStoreService);
}
