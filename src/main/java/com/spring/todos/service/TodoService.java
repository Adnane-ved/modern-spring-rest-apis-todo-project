package com.spring.todos.service;

import com.spring.todos.request.TodoRequest;
import com.spring.todos.response.TodoResponse;
import jakarta.validation.Valid;

import java.util.List;


public interface TodoService {
    TodoResponse createTodo(TodoRequest todoRequest);
    List<TodoResponse> getAllTodos();
    TodoResponse toggleTodoCompletion(Long id);
    void deleteTodo(long id);
}
