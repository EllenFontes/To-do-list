package com.todoapp.todolist.service;


import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.exception.TaskNotFoundException;
import com.todoapp.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public Task create(Task task) {
        return taskRepository.save(task);
    }


    public Task update(Long id, Task newTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setStatus(newTask.getStatus());

        return taskRepository.save(task);

    }



}
