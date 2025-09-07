package com.crudapp.todo_be.model;

import com.crudapp.todo_be.domain.Todo;

import java.io.Serializable;
import java.util.List;

public record TodoResponse(long id, String text, boolean isCompleted) implements Serializable {
    public static TodoResponse from(Todo todo) {
        return new TodoResponse(todo.getId(), todo.getText(), todo.isCompleted());
    }

    public static List<TodoResponse> from(List<Todo> todos) {
         return todos.stream().map(TodoResponse::from).toList();
    }
}
