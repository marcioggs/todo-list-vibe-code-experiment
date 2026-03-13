package com.example.todo.infrastructure.persistence;

import com.example.todo.domain.model.TodoItem;
import com.example.todo.domain.repository.TodoItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TodoItemRepositoryAdapter implements TodoItemRepository {

  private final TodoItemSpringDataRepository repository;

  public TodoItemRepositoryAdapter(TodoItemSpringDataRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<TodoItem> findAllOrderById() {
    return repository.findAllByOrderByIdAsc().stream().map(this::toDomain).toList();
  }

  @Override
  public Optional<TodoItem> findById(Long id) {
    return repository.findById(id).map(this::toDomain);
  }

  @Override
  public TodoItem save(TodoItem todoItem) {
    TodoItemJpaEntity entity = new TodoItemJpaEntity();
    entity.setId(todoItem.id());
    entity.setText(todoItem.text());
    return toDomain(repository.save(entity));
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  private TodoItem toDomain(TodoItemJpaEntity entity) {
    return new TodoItem(entity.getId(), entity.getText());
  }
}
