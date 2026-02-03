package com.todoapp.todolist.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long userId) {super("User with ID " + userId + " doesn't exist");}

}
