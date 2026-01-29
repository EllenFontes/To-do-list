package com.todoapp.todolist.service;

import com.todoapp.todolist.controller.dto.CreateUserDTO;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public User create(CreateUserDTO createUserDTO) {

        Optional<User> emailExists= userRepository.findByEmail(createUserDTO.email());

        if (emailExists.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = new User();
        user.setEmail(createUserDTO.email());
        user.setPassword(bCryptPasswordEncoder.encode(createUserDTO.password()));
        user.setName(createUserDTO.name());

        return userRepository.save(user);
    }

    public User getMyProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return user.get();

    }

}
