package com.todoapp.todolist.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Email " + email + " already exists");
    }
}
