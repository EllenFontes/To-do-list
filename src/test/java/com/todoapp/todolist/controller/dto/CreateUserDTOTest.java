package com.todoapp.todolist.controller.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateUserDTOTest {

    private final Validator validator;

    public CreateUserDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should have validation error when email format is incorrect")
    void shouldHaveErrorWhenEmailIsInvalid() {
        // Arrange
        var dto = new CreateUserDTO("Ellen", "email-invalido", "123456");

        // Act
        var violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format")));
    }
}