package com.example.todo.application;

import com.example.todo.domain.model.TodoItem;
import com.example.todo.domain.repository.TodoItemRepository;
import com.example.todo.domain.repository.TodoListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoItemService {

  private final TodoItemRepository todoItemRepository;
  private final TodoListRepository todoListRepository;

  public TodoItemService(
      TodoItemRepository todoItemRepository, TodoListRepository todoListRepository) {
    this.todoItemRepository = todoItemRepository;
    this.todoListRepository = todoListRepository;
  }

  @Transactional
  public TodoItem create(Long listId, String text) {
    ensureListExists(listId);
    return todoItemRepository.save(new TodoItem(null, listId, text.trim()));
  }

  @Transactional
  public TodoItem update(Long listId, Long id, String text) {
    ensureListExists(listId);
    TodoItem existing =
        todoItemRepository.findById(id).orElseThrow(() -> new TodoItemNotFoundException(id));
    if (!existing.listId().equals(listId)) {
      throw new TodoItemNotFoundException(id);
    }
    return todoItemRepository.save(new TodoItem(id, listId, text.trim()));
  }

  @Transactional
  public void delete(Long listId, Long id) {
    ensureListExists(listId);
    TodoItem existing =
        todoItemRepository.findById(id).orElseThrow(() -> new TodoItemNotFoundException(id));
    if (!existing.listId().equals(listId)) {
      throw new TodoItemNotFoundException(id);
    }
    todoItemRepository.deleteById(id);
  }

  private void ensureListExists(Long listId) {
    todoListRepository.findById(listId).orElseThrow(() -> new TodoListNotFoundException(listId));
  }
}
