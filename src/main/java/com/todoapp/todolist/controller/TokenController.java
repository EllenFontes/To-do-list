package com.todoapp.todolist.controller;

import com.todoapp.todolist.controller.dto.LoginRequestDTO;
import com.todoapp.todolist.controller.dto.LoginResponseDTO;
import com.todoapp.todolist.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class TokenController {

    private final TokenService tokenService;

    public  TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO getUserToken = tokenService.login(loginRequestDTO);
        return ResponseEntity.ok(getUserToken);
    }


}
