package com.todoapp.todolist.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskDTO(
        @NotBlank(message = "Title is required") String taskTitle,
        String taskDescription,
        @NotBlank(message = "Status is required") String taskStatus) {
}
