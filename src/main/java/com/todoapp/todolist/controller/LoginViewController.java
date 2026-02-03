package com.todoapp.todolist.controller;

import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.entity.User;
import com.todoapp.todolist.service.TaskService;
import com.todoapp.todolist.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginViewController {

    private final TaskService taskService;
    private final UserService userService;

    public LoginViewController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
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
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        User user = userService.getMyProfile(userId);
        Long taskCount = taskService.getCompletedTasksCount(userId);

        model.addAttribute("user", user);
        model.addAttribute("completedCount", taskCount);

        return "profile";
    }

}