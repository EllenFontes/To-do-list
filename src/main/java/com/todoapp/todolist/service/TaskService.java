package com.todoapp.todolist.service;


import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public Task create(Task task) {
        return taskRepository.save(task);
    }


}
