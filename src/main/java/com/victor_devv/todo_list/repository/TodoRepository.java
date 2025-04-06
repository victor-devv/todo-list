package com.victor_devv.todo_list.repository;

import com.victor_devv.todo_list.domain.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends BaseRepository<Todo, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<Todo> findById(Long id);

    @EntityGraph(attributePaths = {"user"})
    Page<Todo> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Todo> findByUserIdAndStatus(Long userId, Todo.Status status, Pageable pageable);

}
