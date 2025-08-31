package com.spring.todos.controller;

import com.spring.todos.request.TodoRequest;
import com.spring.todos.response.TodoResponse;
import com.spring.todos.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "todos REST API" , description = "Operations for managing todos")
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;


    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary="Get all todos for user", description="fetch all todo")
    @GetMapping
    public List<TodoResponse> getTodos() {
        return todoService.getAllTodos();
    }

    @Operation(summary=" create todo for user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TodoResponse createTodo(@Valid @RequestBody TodoRequest todoRequest) {
        return todoService.createTodo(todoRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public TodoResponse toggleTodoCompletion(@PathVariable @Min(1) Long id) {
       return todoService.toggleTodoCompletion(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable @Min(1) Long id) {
    todoService.deleteTodo(id);
    }

}
