package com.todoapp.todolist.controller;


import com.todoapp.todolist.controller.dto.CreateUserDTO;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        User newUser = userService.create(createUserDTO);
        return ResponseEntity.ok(newUser);
    }

}
