package com.todoapp.todolist.controller.dto;

import com.todoapp.todolist.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskDTO(
        @NotBlank(message = "Title is required") String taskTitle,
        String taskDescription,
        @NotNull(message = "Status is required") TaskStatus taskStatus) {
}
