import { TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
import { App } from './app';
import { TodoStoreService } from './todo-store.service';

const todoStoreStub = {
  items: signal([]),
  loading: signal(false),
  error: signal(''),
  load: jasmine.createSpy('load'),
  add: jasmine.createSpy('add'),
  update: jasmine.createSpy('update'),
  remove: jasmine.createSpy('remove')
};

describe('App', () => {
  beforeEach(async () => {
    todoStoreStub.load.calls.reset();
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

  it('should render heading', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('TODOs');
  });
});
