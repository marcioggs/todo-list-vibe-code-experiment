package com.example.todo.application;

import com.example.todo.domain.model.TodoItem;
import java.util.List;

public record TodoListWithItems(Long id, String title, List<TodoItem> items) {}
