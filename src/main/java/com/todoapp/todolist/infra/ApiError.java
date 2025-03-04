package com.todoapp.todolist.infra;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class ApiError {

        @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
        LocalDateTime timestamp;

        Integer code;

        String status;

        String message;

        String error;


}
