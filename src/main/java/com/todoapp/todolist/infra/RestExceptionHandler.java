package com.todoapp.todolist.infra;

import com.todoapp.todolist.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    private ResponseEntity<Map<String, Object>> taskNotFoundHandler(TaskNotFoundException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("Error", "Task not found");
        response.put("Message", exception.getMessage());
        response.put("Status", HttpStatus.NOT_FOUND.value());


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
