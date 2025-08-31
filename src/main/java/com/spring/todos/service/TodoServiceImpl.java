package com.spring.todos.service;

import com.spring.todos.entity.Todo;
import com.spring.todos.entity.User;
import com.spring.todos.repository.TodoRepository;
import com.spring.todos.request.TodoRequest;
import com.spring.todos.response.TodoResponse;
import com.spring.todos.util.FindAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    private final FindAuthenticatedUser  findAuthenticatedUser;

    public TodoServiceImpl(TodoRepository todoRepository, FindAuthenticatedUser findAuthenticatedUser) {
        this.todoRepository = todoRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
    }

    @Transactional
    @Override
    public TodoResponse createTodo(TodoRequest todoRequest) {
        User currentUser = findAuthenticatedUser.getAutheticatedUser();

        Todo todo = new Todo(
                todoRequest.getTitle(),
                todoRequest.getDescription(),
                todoRequest.getPriority(),
                false,
                currentUser
        );

        Todo savedTodo = todoRepository.save(todo);

       return convertTodoResponse(savedTodo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> getAllTodos() {
        User currentUser = findAuthenticatedUser.getAutheticatedUser();

        return todoRepository.findByOwner(currentUser).stream()
                .map(this::convertTodoResponse).toList();
    }

    @Override
    @Transactional
    public TodoResponse toggleTodoCompletion(Long id) {
        User currentUser = findAuthenticatedUser.getAutheticatedUser();

        Optional<Todo> todo = todoRepository.findByIdAndOwner(id,currentUser);

        if(todo.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"todo not found");
        }

        todo.get().setComplete(!todo.get().isComplete());
        Todo updatedTodo = todoRepository.save(todo.get());

        return convertTodoResponse(updatedTodo);
    }

    @Override
    @Transactional
    public void deleteTodo(long id){
        User currentUser = findAuthenticatedUser.getAutheticatedUser();

        Optional<Todo> todo = todoRepository.findByIdAndOwner(id,currentUser);

        if(todo.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"todo not found");
        }

        todoRepository.delete(todo.get());
    }

    private TodoResponse convertTodoResponse(Todo todo){
        return new TodoResponse(
               todo.getId(),
               todo.getTitle(),
               todo.getDescription(),
                todo.getPriority(),
                todo.isComplete()
        );
    }
}
