package com.todoapp.todolist.service;

import com.todoapp.todolist.controller.dto.LoginRequestDTO;
import com.todoapp.todolist.controller.dto.LoginResponseDTO;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;


    public TokenService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<User> foundUser = userRepository.findByEmail(loginRequestDTO.email());

        if (foundUser.isEmpty() || !isLoginCorrect(loginRequestDTO, foundUser)) {
            throw new BadCredentialsException("Invalid email or password");
        }

        var currentTime = Instant.now();
        var expiresIn = 3600L;

        var claims = JwtClaimsSet.builder()
                .issuer("toDoListBackend")
                .subject(String.valueOf(foundUser.get().getId()))
                .issuedAt(currentTime)
                .expiresAt(currentTime.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

    public boolean isLoginCorrect(LoginRequestDTO loginRequestDTO, Optional<User> user) {
        return passwordEncoder.matches(loginRequestDTO.password(), user.get().getPassword());
    }

    public Cookie createCookie(LoginResponseDTO loginResponseDTO) {

        Cookie cookie = new Cookie("token", loginResponseDTO.accessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(loginResponseDTO.expiresIn().intValue());

        return cookie;
    }

    public Cookie destroyCookie() {

        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        return cookie;
    }


}
