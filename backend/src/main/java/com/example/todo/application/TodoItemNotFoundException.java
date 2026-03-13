package com.example.todo.application;

public class TodoItemNotFoundException extends RuntimeException {

  public TodoItemNotFoundException(Long id) {
    super("Todo item " + id + " was not found");
  }
}
