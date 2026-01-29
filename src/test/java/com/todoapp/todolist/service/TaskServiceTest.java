package com.todoapp.todolist.service;

import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.exception.TaskNotFoundException;
import com.todoapp.todolist.repository.TaskRepository;
import com.todoapp.todolist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User();
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
    @DisplayName("Should create a task successfully when user exists")
    void create_ShouldReturnTask_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.create(new Task(), 1L);

        assertNotNull(createdTask);
        assertEquals(user, createdTask.getUser());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw an exception when creating a task and user does not exist")
    void create_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.create(new Task(), 1L));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update a task successfully when the user is the owner")
    void update_ShouldUpdateTask_WhenUserIsOwner() {
        Task updatedInfo = new Task();
        updatedInfo.setTitle("Novo Título");
        updatedInfo.setDescription("Nova Descrição");
        updatedInfo.setStatus("COMPLETED");

        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.update(10L, updatedInfo, 1L);

        assertEquals("Novo Título", result.getTitle());
        assertEquals("COMPLETED", result.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw FORBIDDEN when attempting to update another user's task")
    void update_ShouldThrowForbidden_WhenUserIsNotOwner() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> taskService.update(10L, new Task(), 2L)); // Different user ID

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when the task does not exist")
    void update_ShouldThrowNotFound_WhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.update(99L, new Task(), 1L));
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