package com.todoapp.todolist.controller;

import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginViewController {

    private final TaskService taskService;

    public LoginViewController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/tasks-view")
    public String dashboard(Model model, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        List<Task> tasks = taskService.getAllTasks(userId);
        model.addAttribute("listTasks", tasks);
        return "dashboard"; // Nome do arquivo .html
    }
}