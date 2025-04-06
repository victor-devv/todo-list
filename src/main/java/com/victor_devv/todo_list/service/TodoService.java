package com.victor_devv.todo_list.service;

import com.victor_devv.todo_list.domain.dto.TodoDto;
import com.victor_devv.todo_list.domain.dto.TodoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoService {

    TodoDto findById(Long id);

    Page<TodoDto> findAllByUserId(Long userId, Pageable pageable);

    TodoDto create(String username, TodoRequest request);

    TodoDto update(Long id, TodoRequest request);

    void delete(Long id);

    TodoDto markAsCompleted(Long id);

}
