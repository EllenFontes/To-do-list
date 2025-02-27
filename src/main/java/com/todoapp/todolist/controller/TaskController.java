package com.todoapp.todolist.controller;

import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping
    public Task create(@RequestBody Task task) {
        System.out.println("Recebendo a Task: " + task);
        return taskService.create(task);
    }

}
