import { TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
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
  load: jasmine.createSpy('load'),
  createList: jasmine.createSpy('createList'),
  selectList: jasmine.createSpy('selectList'),
  deleteList: jasmine.createSpy('deleteList'),
  updateSelectedListTitle: jasmine.createSpy('updateSelectedListTitle'),
  add: jasmine.createSpy('add'),
  update: jasmine.createSpy('update'),
  remove: jasmine.createSpy('remove')
};

describe('App', () => {
  beforeEach(async () => {
    todoStoreStub.load.calls.reset();
    todoStoreStub.createList.calls.reset();
    todoStoreStub.selectList.calls.reset();
    todoStoreStub.deleteList.calls.reset();
    todoStoreStub.updateSelectedListTitle.calls.reset();
    todoStoreStub.add.calls.reset();
    todoStoreStub.update.calls.reset();
    todoStoreStub.remove.calls.reset();

    await TestBed.configureTestingModule({
      imports: [App],
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
