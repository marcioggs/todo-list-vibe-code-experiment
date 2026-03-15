package com.example.todo.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListSpringDataRepository extends JpaRepository<TodoListJpaEntity, Long> {

  List<TodoListJpaEntity> findAllByOrderByIdAsc();
}
