package com.example.todo.application;

public class TodoListNotFoundException extends RuntimeException {

  public TodoListNotFoundException(Long id) {
    super("Todo list " + id + " was not found");
  }
}
