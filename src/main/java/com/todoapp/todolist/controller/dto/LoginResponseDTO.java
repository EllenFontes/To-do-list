package com.todoapp.todolist.controller.dto;

public record LoginResponseDTO(String accessToken, Long expiresIn) {
}
