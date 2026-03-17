import { TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { vi } from 'vitest';
import { App } from './app';
import { TodoStoreService } from './todo-store.service';

const todoStoreStub = {
  lists: signal([]),
  selectedList: signal(null),
  selectedListId: signal(null),
  selectedListTitle: signal(''),
  selectedItems: signal([]),
  loading: signal(false),
  error: signal(''),
  load: vi.fn(),
  createList: vi.fn(),
  selectList: vi.fn(),
  deleteList: vi.fn(),
  updateSelectedListTitle: vi.fn(),
  add: vi.fn(),
  update: vi.fn(),
  remove: vi.fn()
};

describe('App', () => {
  beforeEach(async () => {
    todoStoreStub.load.mockReset();
    todoStoreStub.createList.mockReset();
    todoStoreStub.selectList.mockReset();
    todoStoreStub.deleteList.mockReset();
    todoStoreStub.updateSelectedListTitle.mockReset();
    todoStoreStub.add.mockReset();
    todoStoreStub.update.mockReset();
    todoStoreStub.remove.mockReset();

    await TestBed.configureTestingModule({
      imports: [App, RouterTestingModule.withRoutes([])],
      providers: [{ provide: TodoStoreService, useValue: todoStoreStub }]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
    expect(todoStoreStub.load).toHaveBeenCalled();
  });

  it('should render the list heading', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.lists-panel h1')?.textContent).toContain('Lists');
  });
});
