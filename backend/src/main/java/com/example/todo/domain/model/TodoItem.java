package com.example.todo.domain.model;

public record TodoItem(Long id, String text) {

  public TodoItem {
    if (text == null || text.isBlank()) {
      throw new IllegalArgumentException("Todo text must not be blank");
    }
  }
}
