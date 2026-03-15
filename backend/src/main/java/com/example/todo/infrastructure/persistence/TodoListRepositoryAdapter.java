package com.example.todo.infrastructure.persistence;

import com.example.todo.domain.model.TodoList;
import com.example.todo.domain.repository.TodoListRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoListRepositoryAdapter implements TodoListRepository {

  private final TodoListSpringDataRepository repository;

  @Override
  public List<TodoList> findAllOrderById() {
    return repository.findAllByOrderByIdAsc().stream().map(this::toDomain).toList();
  }

  @Override
  public Optional<TodoList> findById(Long id) {
    return repository.findById(id).map(this::toDomain);
  }

  @Override
  public TodoList save(TodoList todoList) {
    TodoListJpaEntity entity;
    if (todoList.id() == null) {
      entity = new TodoListJpaEntity();
    } else {
      entity =
          repository
              .findById(todoList.id())
              .orElseThrow(
                  () -> new IllegalStateException("Todo list " + todoList.id() + " was not found"));
    }
    entity.setTitle(todoList.title());
    return toDomain(repository.save(entity));
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  private TodoList toDomain(TodoListJpaEntity entity) {
    return new TodoList(entity.getId(), entity.getTitle());
  }
}
