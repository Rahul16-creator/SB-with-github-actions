package com.crudapp.todo_be.controller;

import com.crudapp.todo_be.model.CreateTodoRequest;
import com.crudapp.todo_be.model.PaginatedResponse;
import com.crudapp.todo_be.model.TodoResponse;
import com.crudapp.todo_be.model.UpdateTodoRequest;
import com.crudapp.todo_be.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos/")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodos() {
        return ResponseEntity.ok().body(todoService.findAllTodos());
    }

    @GetMapping("paginated")
    public ResponseEntity<PaginatedResponse<TodoResponse>> getAllTodosByPagination(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam Sort.Direction sortOrder,
            @RequestParam String sortBy) {
        return ResponseEntity.ok().body(todoService.findAllTodos(page, size, sortOrder, sortBy));
    }


    @GetMapping("{id}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable long id) {
        return new ResponseEntity<>(todoService.findTodoById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@RequestBody CreateTodoRequest createTodoRequest) {
        return new ResponseEntity<>(todoService.addTodo(createTodoRequest), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<TodoResponse> updateTodoById(@PathVariable long id, @RequestBody UpdateTodoRequest updateTodoRequest) {
        return new ResponseEntity<>(todoService.updateTodoById(id, updateTodoRequest), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable long id) {
        todoService.deleteTodoById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
