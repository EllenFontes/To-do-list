package com.todoapp.todolist.config;

import com.todoapp.todolist.entity.Task;
import com.todoapp.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Autowired
    private TaskRepository taskRepository;

    @Bean
    CommandLineRunner loadData() {
        return args -> {
            if (taskRepository.count() == 0) { // Evita duplicar dados
                Task task1 = new Task();
                task1.setTitle("Estudar Spring Boot");
                task1.setDescription("Fazer um projeto To-Do List");
                task1.setStatus("PENDENTE");
                taskRepository.save(task1);

                Task task2 = new Task();
                task2.setTitle("Fazer Exercícios");
                task2.setDescription("Academia às 18h");
                task2.setStatus("CONCLUÍDO");
                taskRepository.save(task2);

                System.out.println("Tasks adicionadas com sucesso!");
            }
        };
    }
}
