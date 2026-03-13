package com.example.todo.interfaces.rest;

import com.example.todo.application.TodoItemService;
import com.example.todo.domain.model.TodoItem;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoItemController {

  private final TodoItemService todoItemService;

  public TodoItemController(TodoItemService todoItemService) {
    this.todoItemService = todoItemService;
  }

  @GetMapping
  public List<TodoItemResponse> findAll() {
    return todoItemService.findAll().stream().map(this::toResponse).toList();
  }

  @PostMapping
  public TodoItemResponse create(@Valid @RequestBody TodoItemRequest request) {
    return toResponse(todoItemService.create(request.text()));
  }

  @PutMapping("/{id}")
  public TodoItemResponse update(@PathVariable Long id, @Valid @RequestBody TodoItemRequest request) {
    return toResponse(todoItemService.update(id, request.text()));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    todoItemService.delete(id);
  }

  private TodoItemResponse toResponse(TodoItem todoItem) {
    return new TodoItemResponse(todoItem.id(), todoItem.text());
  }
}
