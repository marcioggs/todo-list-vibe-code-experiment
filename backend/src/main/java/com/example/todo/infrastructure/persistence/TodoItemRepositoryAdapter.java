package com.example.todo.infrastructure.persistence;

import com.example.todo.domain.model.TodoItem;
import com.example.todo.domain.repository.TodoItemRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoItemRepositoryAdapter implements TodoItemRepository {

  private final TodoItemSpringDataRepository repository;

  @Override
  public List<TodoItem> findAllByListIdOrderById(Long listId) {
    return repository.findByListIdOrderByIdAsc(listId).stream().map(this::toDomain).toList();
  }

  @Override
  public List<TodoItem> findAllOrderByListIdAndId() {
    return repository.findAllByOrderByListIdAscIdAsc().stream().map(this::toDomain).toList();
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
    entity.setListId(todoItem.listId());
    return toDomain(repository.save(entity));
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  @Override
  public void deleteByListId(Long listId) {
    repository.deleteAllByListId(listId);
  }

  private TodoItem toDomain(TodoItemJpaEntity entity) {
    return new TodoItem(entity.getId(), entity.getListId(), entity.getText());
  }
}
