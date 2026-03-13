package com.example.todo.interfaces.rest;

import com.example.todo.application.TodoItemNotFoundException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(TodoItemNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(TodoItemNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException exception) {
    return ResponseEntity.badRequest().body(Map.of("message", "Todo text must not be blank"));
  }
}
