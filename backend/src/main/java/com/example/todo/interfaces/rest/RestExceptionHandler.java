package com.example.todo.interfaces.rest;

import com.example.todo.application.TodoItemNotFoundException;
import com.example.todo.application.TodoListNotFoundException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler({TodoItemNotFoundException.class, TodoListNotFoundException.class})
  public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("message", exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(
      MethodArgumentNotValidException exception) {
    var fieldError = exception.getBindingResult().getFieldError();
    String field = fieldError != null ? fieldError.getField() : "";
    String message =
        "title".equals(field) ? "List title must not be blank" : "Todo text must not be blank";
    return ResponseEntity.badRequest().body(Map.of("message", message));
  }
}
