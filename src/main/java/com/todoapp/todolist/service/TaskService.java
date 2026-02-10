package com.todoapp.todolist.service;


import com.todoapp.todolist.controller.dto.CreateTaskDTO;
import com.todoapp.todolist.controller.dto.UpdateTaskDTO;
import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.exception.AccessDeniedException;
import com.todoapp.todolist.exception.TaskNotFoundException;
import com.todoapp.todolist.exception.UserNotFoundException;
import com.todoapp.todolist.repository.TaskRepository;
import com.todoapp.todolist.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.todoapp.todolist.entity.enums.TaskStatus;

import java.util.List;

@Service
public class TaskService {

    TaskRepository taskRepository;
    UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    public Task create(CreateTaskDTO createTaskDTO, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Task task = new Task();
        task.setTitle(createTaskDTO.taskTitle());
        task.setDescription(createTaskDTO.taskDescription());
        task.setStatus(createTaskDTO.taskStatus());
        task.setUser(user);

        return taskRepository.save(task);
    }


    public Task update(Long taskId, UpdateTaskDTO updateTaskDTO, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (task.getUser().getId() != userId) {
            throw new AccessDeniedException("You don't have permission to access this task");
        }

        task.setTitle(updateTaskDTO.taskTitle());
        task.setDescription(updateTaskDTO.taskDescription());
        task.setStatus(updateTaskDTO.taskStatus());

        return taskRepository.save(task);

    }


    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }


    public long getCompletedTasksCount(Long userId){
        List<Task> tasks = getAllTasks(userId);

        return tasks.stream()
                .filter(t -> TaskStatus.COMPLETED.equals(t.getStatus()))
                .count();
    }

}
