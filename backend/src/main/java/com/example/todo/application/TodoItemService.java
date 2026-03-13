package com.example.todo.application;

import com.example.todo.domain.model.TodoItem;
import com.example.todo.domain.repository.TodoItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoItemService {

  private final TodoItemRepository todoItemRepository;

  public TodoItemService(TodoItemRepository todoItemRepository) {
    this.todoItemRepository = todoItemRepository;
  }

  @Transactional(readOnly = true)
  public List<TodoItem> findAll() {
    return todoItemRepository.findAllOrderById();
  }

  @Transactional
  public TodoItem create(String text) {
    return todoItemRepository.save(new TodoItem(null, text.trim()));
  }

  @Transactional
  public TodoItem update(Long id, String text) {
    todoItemRepository.findById(id).orElseThrow(() -> new TodoItemNotFoundException(id));
    return todoItemRepository.save(new TodoItem(id, text.trim()));
  }

  @Transactional
  public void delete(Long id) {
    todoItemRepository.findById(id).orElseThrow(() -> new TodoItemNotFoundException(id));
    todoItemRepository.deleteById(id);
  }
}
