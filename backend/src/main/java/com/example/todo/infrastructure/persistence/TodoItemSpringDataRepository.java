package com.example.todo.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoItemSpringDataRepository extends JpaRepository<TodoItemJpaEntity, Long> {

  List<TodoItemJpaEntity> findByListIdOrderByIdAsc(Long listId);

  List<TodoItemJpaEntity> findAllByOrderByListIdAscIdAsc();

  void deleteAllByListId(Long listId);
}
