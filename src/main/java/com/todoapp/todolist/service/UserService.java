package com.todoapp.todolist.service;

import com.todoapp.todolist.controller.dto.CreateUserDTO;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.exception.UserAlreadyExistsException;
import com.todoapp.todolist.exception.UserNotFoundException;
import com.todoapp.todolist.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public User create(CreateUserDTO createUserDTO) {

        userRepository.findByEmail(createUserDTO.email())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException(createUserDTO.email());
                });

        User user = new User();
        user.setEmail(createUserDTO.email());
        user.setPassword(bCryptPasswordEncoder.encode(createUserDTO.password()));
        user.setName(createUserDTO.name());

        return userRepository.save(user);
    }

    public User getMyProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}
