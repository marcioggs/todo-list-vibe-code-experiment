import { TodoItem } from './todo-item.model';

export interface TodoList {
  id: number;
  title: string;
  items: TodoItem[];
}
