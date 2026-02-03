package com.todoapp.todolist.controller;

import com.todoapp.todolist.controller.dto.CreateTaskDTO;
import com.todoapp.todolist.controller.dto.UpdateTaskDTO;
import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO, @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        Task newTask = taskService.create(createTaskDTO, userId);
        return ResponseEntity.ok(newTask);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") Long taskId,
                                           @Valid @RequestBody UpdateTaskDTO updateTaskDTO,
                                           @AuthenticationPrincipal Jwt jwt){
        Long userId = Long.valueOf(jwt.getSubject());

        Task updatedTask = taskService.update(taskId, updateTaskDTO, userId);
        return ResponseEntity.ok(updatedTask);
    }


    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@AuthenticationPrincipal Jwt jwt){
        Long userId = Long.valueOf(jwt.getSubject());

        List<Task> listTasks = taskService.getAllTasks(userId);
        return ResponseEntity.ok(listTasks);
    }


}
