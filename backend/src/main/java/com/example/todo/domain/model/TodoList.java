package com.example.todo.domain.model;

import java.util.Objects;

public record TodoList(Long id, String title) {

  public TodoList {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("List title must not be blank");
    }
  }
}
