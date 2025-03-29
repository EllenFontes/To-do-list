package com.todoapp.todolist.controller;

import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task newTask = taskService.create(task);
        return ResponseEntity.ok(newTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task){
        Task updatedTask = taskService.update(id, task);
        return ResponseEntity.ok(updatedTask);
    }

}
