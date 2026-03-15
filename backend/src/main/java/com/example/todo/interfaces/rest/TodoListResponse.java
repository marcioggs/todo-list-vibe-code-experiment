package com.example.todo.interfaces.rest;

import java.util.List;

public record TodoListResponse(Long id, String title, List<TodoItemResponse> items) {}
