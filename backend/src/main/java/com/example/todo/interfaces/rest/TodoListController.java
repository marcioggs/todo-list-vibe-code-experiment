package com.example.todo.interfaces.rest;

import com.example.todo.application.TodoItemService;
import com.example.todo.application.TodoListService;
import com.example.todo.application.TodoListWithItems;
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
@RequestMapping("/api/lists")
public class TodoListController {

  private final TodoListService todoListService;
  private final TodoItemService todoItemService;

  public TodoListController(TodoListService todoListService, TodoItemService todoItemService) {
    this.todoListService = todoListService;
    this.todoItemService = todoItemService;
  }

  @GetMapping
  public List<TodoListResponse> findAll() {
    return todoListService.findAll().stream().map(this::toResponse).toList();
  }

  @PostMapping
  public TodoListResponse create(@Valid @RequestBody TodoListRequest request) {
    return toResponse(todoListService.create(request.title()));
  }

  @PutMapping("/{id}")
  public TodoListResponse update(
      @PathVariable Long id, @Valid @RequestBody TodoListRequest request) {
    return toResponse(todoListService.update(id, request.title()));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    todoListService.delete(id);
  }

  @PostMapping("/{listId}/todos")
  public TodoItemResponse addItem(
      @PathVariable Long listId, @Valid @RequestBody TodoItemRequest request) {
    return toItemResponse(todoItemService.create(listId, request.text()));
  }

  @PutMapping("/{listId}/todos/{id}")
  public TodoItemResponse updateItem(
      @PathVariable Long listId,
      @PathVariable Long id,
      @Valid @RequestBody TodoItemRequest request) {
    return toItemResponse(todoItemService.update(listId, id, request.text()));
  }

  @DeleteMapping("/{listId}/todos/{id}")
  public void deleteItem(@PathVariable Long listId, @PathVariable Long id) {
    todoItemService.delete(listId, id);
  }

  private TodoListResponse toResponse(TodoListWithItems list) {
    return new TodoListResponse(
        list.id(), list.title(), list.items().stream().map(this::toItemResponse).toList());
  }

  private TodoItemResponse toItemResponse(TodoItem todoItem) {
    return new TodoItemResponse(todoItem.id(), todoItem.listId(), todoItem.text());
  }
}
