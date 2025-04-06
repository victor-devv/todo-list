package com.victor_devv.todo_list.service.impl;

import com.victor_devv.todo_list.domain.dto.TodoDto;
import com.victor_devv.todo_list.domain.dto.TodoRequest;
import com.victor_devv.todo_list.domain.entity.Todo;
import com.victor_devv.todo_list.domain.entity.User;
import com.victor_devv.todo_list.domain.mapper.TodoMapper;
import com.victor_devv.todo_list.repository.TodoRepository;
import com.victor_devv.todo_list.repository.UserRepository;
import com.victor_devv.todo_list.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TodoMapper todoMapper;

    @Override
    @Transactional(readOnly = true)
    public TodoDto findById(Long id) {
        return todoRepository.findById(id)
                .map(todoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TodoDto> findAllByUserId(Long userId, Pageable pageable) {
        checkUserExists(userId);
        return todoRepository.findByUserId(userId, pageable)
                .map(todoMapper::toDto);
    }

    @Override
    @Transactional
    public TodoDto create(String email, TodoRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));

        Todo todo = todoMapper.toEntity(request);
        todo.setUser(user);

        Todo savedTodo = todoRepository.save(todo);
        log.info("Created Todo with id: {}", savedTodo.getId());

        return todoMapper.toDto(savedTodo);
    }

    @Override
    @Transactional
    public TodoDto update(Long id, TodoRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));

        todoMapper.updateEntityFromRequest(request, todo);

        Todo savedTodo = todoRepository.save(todo);
        log.info("Updated Todo with id: {}", savedTodo.getId());

        return todoMapper.toDto(savedTodo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));

        todoRepository.delete(todo);
        log.info("Deleted Todo with id: {}", id);
    }

    @Override
    @Transactional
    public TodoDto markAsCompleted(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));

        todo.setStatus(Todo.Status.COMPLETED);
        todo.setCompletedAt(LocalDateTime.now());

        Todo savedTodo = todoRepository.save(todo);
        log.info("Marked Todo as completed with id: {}", id);

        return todoMapper.toDto(savedTodo);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }
}
