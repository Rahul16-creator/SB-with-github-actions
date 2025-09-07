package com.crudapp.todo_be.model;

public record UpdateTodoRequest(String text, boolean isCompleted) {
}
