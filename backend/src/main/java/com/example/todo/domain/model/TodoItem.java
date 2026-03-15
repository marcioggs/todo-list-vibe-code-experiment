package com.example.todo.domain.model;

public record TodoItem(Long id, Long listId, String text) {

  public TodoItem {
    if (listId == null) {
      throw new IllegalArgumentException("Todo must be associated with a list");
    }
    if (text == null || text.isBlank()) {
      throw new IllegalArgumentException("Todo text must not be blank");
    }
  }
}
