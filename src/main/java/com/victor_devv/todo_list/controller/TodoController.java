package com.victor_devv.todo_list.controller;

import com.victor_devv.todo_list.config.jwt.JwtService;
import com.victor_devv.todo_list.controller.advice.ApiResponseBuilder;
import com.victor_devv.todo_list.controller.advice.jsend.JSendResponse;
import com.victor_devv.todo_list.domain.dto.TodoDto;
import com.victor_devv.todo_list.domain.dto.TodoRequest;
import com.victor_devv.todo_list.service.TodoService;
import com.victor_devv.todo_list.util.Constants;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
@Tag(name = "Todo", description = "Todo management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TodoController {

    private final TodoService todoService;
    private final JwtService jwtService;

    @GetMapping("/{id}")
    @Timed(value = "todo.findById", description = "Time taken to find a todo by id")
    public ResponseEntity<JSendResponse> findById(HttpServletRequest request, @PathVariable Long id) {
        TodoDto todoDto = todoService.findById(id);
        String token = jwtService.extractTokenFromRequest(request);
        Long userId = jwtService.extractUserId(token);
        if (!todoDto.getUserId().equals(userId)) {
            return ApiResponseBuilder.fail(null, HttpStatus.FORBIDDEN);
        }
        return ApiResponseBuilder.success(todoDto);
    }

    @GetMapping
    @Timed(value = "todo.findAll", description = "Time taken to find all todos belonging to a user")
    public ResponseEntity<JSendResponse> findAll(
            HttpServletRequest request,
            @PageableDefault(size = Constants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        String token = jwtService.extractTokenFromRequest(request);
        Long userId = jwtService.extractUserId(token);
        return ApiResponseBuilder.success(todoService.findAllByUserId(userId, pageable));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Timed(value = "todo.create", description = "Time taken to create a todo item")
    public ResponseEntity<JSendResponse> create(
            @Valid @RequestBody TodoRequest payload,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ApiResponseBuilder.success(todoService.create(userDetails.getUsername(), payload), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Timed(value = "todo.update", description = "Time taken to update a todo item")
    public ResponseEntity<JSendResponse> update(
            HttpServletRequest request,
            @PathVariable Long id,
            @Valid @RequestBody TodoRequest payload) {
        TodoDto existingTodo = todoService.findById(id);
        String token = jwtService.extractTokenFromRequest(request);
        Long userId = jwtService.extractUserId(token);
        if (!existingTodo.getUserId().equals(userId)) {
            return ApiResponseBuilder.fail(null, HttpStatus.FORBIDDEN);
        }
        return ApiResponseBuilder.success(todoService.update(id, payload));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed(value = "todo.delete", description = "Time taken to delete a todo")
    public ResponseEntity<Void> delete(
            HttpServletRequest request,
            @PathVariable Long id) {
        TodoDto existingTodo = todoService.findById(id);
        String token = jwtService.extractTokenFromRequest(request);
        Long userId = jwtService.extractUserId(token);
        if (!existingTodo.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    @Timed(value = "todo.complete", description = "Time taken to mark a todo as completed")
    public ResponseEntity<JSendResponse> markAsCompleted(
            HttpServletRequest request,
            @PathVariable Long id) {
        TodoDto existingTodo = todoService.findById(id);
        String token = jwtService.extractTokenFromRequest(request);
        Long userId = jwtService.extractUserId(token);
        if (!existingTodo.getUserId().equals(userId)) {
            return ApiResponseBuilder.fail(null, HttpStatus.FORBIDDEN);
        }
        return ApiResponseBuilder.success(todoService.markAsCompleted(id));
    }
}
