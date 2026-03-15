package com.example.todo.interfaces.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoListRequest(@NotBlank @Size(max = 255) String title) {}
