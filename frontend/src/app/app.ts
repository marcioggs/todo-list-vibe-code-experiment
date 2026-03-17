import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TodoStoreService } from './todo-store.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class App {
  protected readonly store = inject(TodoStoreService);
  protected readonly newListTitle = signal('');
  private readonly router = inject(Router);

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

  protected navigateToList(id: number): void {
    this.router.navigate([id]);
  }

  protected deleteList(id: number): void {
    this.store.deleteList(id);
  }
}
