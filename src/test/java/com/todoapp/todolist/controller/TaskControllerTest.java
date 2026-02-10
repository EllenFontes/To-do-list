package com.todoapp.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.todolist.controller.dto.CreateTaskDTO;
import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.entity.enums.TaskStatus;
import com.todoapp.todolist.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;


    @Test
    @DisplayName("Should create task successfully")
    void createTask_ShouldReturnOk_WhenDataIsValid() throws Exception{
        //Arrange

        CreateTaskDTO createTaskDTO = new CreateTaskDTO("Tarefa Teste", "Desc", TaskStatus.PENDING);

        Task createdTask = new Task();
        createdTask.setId(1L);
        createdTask.setTitle("Tarefa Teste");
        createdTask.setDescription("Desc");
        createdTask.setStatus(TaskStatus.PENDING);

        when(taskService.create(any(CreateTaskDTO.class), eq(1L))).thenReturn(createdTask);


        // 2. ACT
        mockMvc.perform(post("/tasks")
                        .with(jwt().jwt(builder -> builder.subject("1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))

                // 3. ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Tarefa Teste"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }


    @Test
    @DisplayName("Should return bad request when title is invalid")
    void createTask_ShouldReturnBadRequest_WhenTitleIsInvalid() throws  Exception {

        CreateTaskDTO dto = new CreateTaskDTO("", "description", TaskStatus.PENDING );

        mockMvc.perform(post("/tasks")
                .with(jwt().jwt(builder -> builder.subject("1")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isBadRequest());

    }
}