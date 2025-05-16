package com.todoapp.todolist.service;


import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.exception.TaskNotFoundException;
import com.todoapp.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    //Cria uma task

    public Task create(Task task) {
        return taskRepository.save(task);
    }


    //Atualiza uma task existente

    public Task update(Long id, Task newTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setStatus(newTask.getStatus());

        return taskRepository.save(task);

    }

    //Mostrar todas as tasks

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }



}
