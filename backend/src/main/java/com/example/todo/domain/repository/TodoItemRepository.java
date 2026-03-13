package com.example.todo.domain.repository;

import com.example.todo.domain.model.TodoItem;
import java.util.List;
import java.util.Optional;

public interface TodoItemRepository {

  List<TodoItem> findAllOrderById();

  Optional<TodoItem> findById(Long id);

  TodoItem save(TodoItem todoItem);

  void deleteById(Long id);
}
