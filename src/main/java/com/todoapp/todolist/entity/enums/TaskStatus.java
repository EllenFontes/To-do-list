package com.todoapp.todolist.entity.enums;

public enum TaskStatus {
    PENDING("Pendente"),
    IN_PROGRESS("Em Andamento"),
    COMPLETED("Concluído");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
