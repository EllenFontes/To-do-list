package com.todoapp.todolist.infra;

import com.todoapp.todolist.exception.AccessDeniedException;
import com.todoapp.todolist.exception.TaskNotFoundException;
import com.todoapp.todolist.exception.UserAlreadyExistsException;
import com.todoapp.todolist.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    //Tratamento de erro: validações dos campos enviados

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        StringJoiner joiner = new StringJoiner("; ");

        for (var error : exception.getBindingResult().getAllErrors()) {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            joiner.add(field + ": " + message);
        }

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .message("Validation has failed for one or more fields")
                .error(joiner.toString())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    //Tratamento de erro: Task não encontrada

    @ExceptionHandler(TaskNotFoundException.class)
    private ResponseEntity<ApiError> taskNotFoundHandler(TaskNotFoundException exception){
        ApiError apiError = ApiError
                .builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .error("Task not found")
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ApiError> userNotFoundHandler(UserNotFoundException exception){
        ApiError apiError = ApiError
                .builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .error("User not found")
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ResponseEntity<ApiError> userAlreadyExistsHandler(UserAlreadyExistsException exception){
        ApiError apiError = ApiError
                .builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT.name())
                .message(exception.getMessage())
                .error("User already exists")
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ApiError> accessDeniedHandler(AccessDeniedException exception) {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN.name())
                .message(exception.getMessage())
                .error("Access denied")
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}
