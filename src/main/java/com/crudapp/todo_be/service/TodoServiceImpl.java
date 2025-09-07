package com.crudapp.todo_be.service;

import com.crudapp.todo_be.domain.Todo;
import com.crudapp.todo_be.exception.BadRequestException;
import com.crudapp.todo_be.model.CreateTodoRequest;
import com.crudapp.todo_be.model.PaginatedResponse;
import com.crudapp.todo_be.model.TodoResponse;
import com.crudapp.todo_be.model.UpdateTodoRequest;
import com.crudapp.todo_be.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    @Cacheable("todos")
    public List<TodoResponse> findAllTodos() {
        return todoRepository.findAll().stream().map(TodoResponse::from).toList();
    }

    @Override
    @Cacheable(value = "paginatedTodoResponse",
            key = "T(com.crudapp.todo_be.utils.CacheKeyUtil).generateKey(#pageNumber, #pageSize, #sortOrder, #sortBy)"
    )
    public PaginatedResponse<TodoResponse> findAllTodos(
            int pageNumber, int pageSize, Sort.Direction sortOrder, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortOrder, sortBy));

        Page<Todo> paginatedData = todoRepository.findAll(pageable);
        List<TodoResponse> todoResponses = TodoResponse.from(paginatedData.getContent());

        return new PaginatedResponse<TodoResponse>(
                todoResponses,
                paginatedData.getTotalElements(),
                paginatedData.getTotalPages(),
                paginatedData.getNumber(),
                paginatedData.getSize()
        );
    }

    @Override
    @Cacheable(value = "todoById", key = "#id")
    public TodoResponse findTodoById(long id) {
        return TodoResponse.from(findByIdOrThrow(id));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "todos", allEntries = true),
            @CacheEvict(value = "paginatedTodoResponse", allEntries = true)
    }, put = {
            @CachePut(value = "todoById", key = "#result.id()")
    })
    public TodoResponse addTodo(CreateTodoRequest createTodoRequest) {
        Todo todo = Todo.builder().text(createTodoRequest.text()).completed(false).build();
        Todo createdTodo = todoRepository.save(todo);
        return TodoResponse.from(createdTodo);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(allEntries = true, value = "todos"),
            @CacheEvict(value = "paginatedTodoResponse", allEntries = true)
    }, put = {
            @CachePut(value = "todoById", key = "#result.id()")
    })
    public TodoResponse updateTodoById(long id, UpdateTodoRequest updateTodoRequest) {
        Todo existingTodo = findByIdOrThrow(id);
        existingTodo.setText(updateTodoRequest.text());
        existingTodo.setCompleted(updateTodoRequest.isCompleted());
        Todo updatedTodo = todoRepository.save(existingTodo);
        return TodoResponse.from(updatedTodo);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "todos", allEntries = true),
            @CacheEvict(value = "todoById", key = "#id"),
            @CacheEvict(value = "paginatedTodoResponse", allEntries = true)
    })
    public void deleteTodoById(long id) {
        Todo todo = findByIdOrThrow(id);
        todoRepository.delete(todo);
        return;
    }

    // Private methods
    public Todo findByIdOrThrow(long id) {
        return todoRepository.findById(id).orElseThrow(() -> new BadRequestException("Todo Not Found"));
    }
}
