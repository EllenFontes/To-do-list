package com.todoapp.todolist.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateTaskDTO(@NotBlank(message = "Title is required") String title,
                            String description,
                            @NotBlank(message = "Status is required") String status) {
}
