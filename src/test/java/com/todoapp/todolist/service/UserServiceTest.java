package com.todoapp.todolist.service;

import com.todoapp.todolist.controller.dto.CreateUserDTO;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.exception.UserAlreadyExistsException;
import com.todoapp.todolist.exception.UserNotFoundException;
import com.todoapp.todolist.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should create a user with success")
    void shouldCreateUserWithSuccess() {
        // Arrange (Preparação)
        var dto = new CreateUserDTO("Ellen", "ellen@test.com", "123456");
        var user = new User();
        user.setId(1L);
        user.setName(dto.name());
        user.setEmail(dto.email());

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.password())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act (Execução)
        var result = userService.create(dto);

        // Assert (Verificação)
        assertNotNull(result);
        assertEquals(dto.email(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

        verify(passwordEncoder, times(1)).encode(dto.password());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Arrange
        var dto = new CreateUserDTO("Ellen", "ellen@test.com", "123456");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.create(dto));
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("Should return user data when id is found")
    void shouldReturnUserProfile_WhenIdIsFound() {
        //Arrange

        User user = new User();
        user.setId(1L);
        user.setName("Ellen");
        user.setPassword("encoded_password");
        user.setEmail("ellen@email.com");

        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        //Act

        User result = userService.getMyProfile(id);

        //Assert

        verify(userRepository, times(1)).findById(id);
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
    }


    @Test
    @DisplayName("Should return UserNotFoundException when id is not found")
    void shouldThrowException_WhenIdIsNotFound() {
        //Arrange
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //Act & assert
        assertThrows(UserNotFoundException.class, () -> userService.getMyProfile(id));


    }



}