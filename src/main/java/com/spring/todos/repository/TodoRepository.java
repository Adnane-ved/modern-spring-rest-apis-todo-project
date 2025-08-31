package com.spring.todos.repository;

import com.spring.todos.entity.Todo;
import com.spring.todos.entity.User;
import com.spring.todos.request.TodoRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {
 List<Todo> findByOwner(User owner);
 Optional<Todo> findByIdAndOwner(Long id, User owner);
}
