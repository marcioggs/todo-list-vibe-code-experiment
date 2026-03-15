import { Routes } from '@angular/router';
import { ListPlaceholderComponent } from './list-placeholder.component';
import { ListViewComponent } from './list-view.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: ListPlaceholderComponent },
  { path: ':id', component: ListViewComponent }
];
