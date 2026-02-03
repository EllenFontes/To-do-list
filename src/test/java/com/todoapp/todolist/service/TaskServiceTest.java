package com.todoapp.todolist.service;

import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setName("Ellen");
        user.setEmail("ellen@email.com");

        task = new Task();
        task.setId(10L);
        task.setTitle("Estudar Testes");
        task.setDescription("Criar testes unitários para o TaskService");
        task.setStatus("PENDING");
        task.setUser(user);
    }


    @Test
    @DisplayName("Should return a list of tasks for a specific user")
    void getAllTasks_ShouldReturnUserTasks() {
        when(taskRepository.findByUserId(1L)).thenReturn(List.of(task));

        List<Task> tasks = taskService.getAllTasks(1L);

        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        verify(taskRepository).findByUserId(1L);
    }
}