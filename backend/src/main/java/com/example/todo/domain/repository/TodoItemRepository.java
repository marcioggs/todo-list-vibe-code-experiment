package com.example.todo.domain.repository;

import com.example.todo.domain.model.TodoItem;
import java.util.List;
import java.util.Optional;

public interface TodoItemRepository {

  List<TodoItem> findAllByListIdOrderById(Long listId);

  List<TodoItem> findAllOrderByListIdAndId();

  Optional<TodoItem> findById(Long id);

  TodoItem save(TodoItem todoItem);

  void deleteById(Long id);

  void deleteByListId(Long listId);
}
