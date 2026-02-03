package com.todoapp.todolist.service;


import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.exception.TaskNotFoundException;
import com.todoapp.todolist.repository.TaskRepository;
import com.todoapp.todolist.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    TaskRepository taskRepository;
    UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    public Task create(Task task, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUser(user);

        return taskRepository.save(task);
    }


    public Task update(Long taskId, Task newTask, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (task.getUser().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to update this task");
        }

        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setStatus(newTask.getStatus());

        return taskRepository.save(task);

    }


    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public long getCompletedTasksCount(Long userId){
        List<Task> tasks = getAllTasks(userId);

        return tasks.stream()
                .filter(t -> "COMPLETED".equals(t.getStatus()))
                .count();
    }



}
