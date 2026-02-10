package com.todoapp.todolist.service;

import com.todoapp.todolist.controller.dto.CreateTaskDTO;
import com.todoapp.todolist.controller.dto.UpdateTaskDTO;
import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.entity.enums.TaskStatus;
import com.todoapp.todolist.exception.AccessDeniedException;
import com.todoapp.todolist.exception.UserNotFoundException;
import com.todoapp.todolist.repository.TaskRepository;
import com.todoapp.todolist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(20L);
        user.setName("Ellen");
        user.setEmail("ellen@email.com");

        task = new Task();
        task.setId(30L);
        task.setTitle("Estudar Testes");
        task.setDescription("Criar testes unitários para o TaskService");
        task.setStatus(TaskStatus.PENDING);
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

    @Test
    @DisplayName("Should create task successfully when user exists")
    void create_ShouldSaveTask_WhenUserExists() {
        //Arrange
        CreateTaskDTO taskDTO = new CreateTaskDTO("Nova Tarefa", "Desc", TaskStatus.PENDING);
        User userMock = new User();
        userMock.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userMock));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        //Act
        Task createdTask = taskService.create(taskDTO, 1L);

        //Assert
        assertNotNull(createdTask);
        assertEquals("Nova Tarefa", createdTask.getTitle());
        assertEquals(1L, createdTask.getUser().getId());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user is not found")
    void create_ShouldThrowException_WhenUserNotFound() {
        //Arrange
        CreateTaskDTO createTaskDTO = new CreateTaskDTO("Nova Tarefa", "Desc", TaskStatus.PENDING);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        //Act
        assertThrows(UserNotFoundException.class, () -> taskService.create(createTaskDTO, 99L));

        //Assert

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update task when user owns task")
    void update_ShouldUpdateTask_WhenUserIsOwner() {
        //Arrange
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO("Nova Tarefa Editada", "Desc Editada", TaskStatus.PENDING);

        User userMock = new User();
        userMock.setId(1L);

        Task task = new Task();
        task.setId(10L);
        task.setTitle("Tarefa antiga");
        task.setDescription("Descrição antiga");
        task.setStatus(TaskStatus.COMPLETED);
        task.setUser(userMock);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        //Act
        Task updatedTask = taskService.update(task.getId(), updateTaskDTO, userMock.getId());

        //Assert
        verify(taskRepository, times(1)).save(any(Task.class));
        assertEquals("Nova Tarefa Editada", updatedTask.getTitle());
        assertEquals("Desc Editada", updatedTask.getDescription());
        assertEquals(TaskStatus.COMPLETED, updatedTask.getStatus());
        assertEquals(userMock, updatedTask.getUser());

    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user doesn't own task")
    void update_ShouldThrowException_WhenUserIsNotOwner() {
        //Arrange

        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO("Nova Tarefa Editada", "Desc Editada", TaskStatus.PENDING);
        User userMockOwner = new User();
        userMockOwner.setId(1L);

        User userMockNotOwner = new User();
        userMockNotOwner.setId(99L);

        Task taskMock = new Task();
        taskMock.setId(10L);
        taskMock.setTitle("Estudar Testes");
        taskMock.setDescription("estudar");
        taskMock.setUser(userMockOwner);

        when(taskRepository.findById(taskMock.getId())).thenReturn(Optional.of(taskMock));

        //Act

        assertThrows(AccessDeniedException.class,
                () -> taskService.update(taskMock.getId(), updateTaskDTO, userMockNotOwner.getId()));

        //Asset

        verify(taskRepository, never()).save(any(Task.class));
        assertEquals("Estudar Testes", taskMock.getTitle());
    }

}