package com.todoapp.todolist.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id){
        super("Task with ID " + id + " doesn't exist");
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
