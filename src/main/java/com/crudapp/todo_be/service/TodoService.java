package com.crudapp.todo_be.service;

import com.crudapp.todo_be.model.CreateTodoRequest;
import com.crudapp.todo_be.model.PaginatedResponse;
import com.crudapp.todo_be.model.TodoResponse;
import com.crudapp.todo_be.model.UpdateTodoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TodoService {
    List<TodoResponse> findAllTodos();

    PaginatedResponse<TodoResponse> findAllTodos(int pageNumber, int pageSize, Sort.Direction sortOrder, String sortBy);

    TodoResponse findTodoById(long id);

    TodoResponse addTodo(CreateTodoRequest createTodoRequest);

    TodoResponse updateTodoById(long id, UpdateTodoRequest updateTodoRequest);

    void deleteTodoById(long id);
}
