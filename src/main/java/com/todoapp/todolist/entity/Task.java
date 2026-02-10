package com.todoapp.todolist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todoapp.todolist.entity.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "DB_TASK")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TASK_ID")
    private long id;

    @NotBlank(message = "Title is required")
    @Column(name = "TASK_TITLE", nullable = false)
    private String title;

    @Column(name = "TASK_DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "TASK_STATUS", nullable = false)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TASK_USER_ID")
    @JsonIgnore
    private User user;

}
