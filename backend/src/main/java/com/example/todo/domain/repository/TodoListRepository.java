package com.example.todo.domain.repository;

import com.example.todo.domain.model.TodoList;
import java.util.List;
import java.util.Optional;

public interface TodoListRepository {

  List<TodoList> findAllOrderById();

  Optional<TodoList> findById(Long id);

  TodoList save(TodoList todoList);

  void deleteById(Long id);
}
