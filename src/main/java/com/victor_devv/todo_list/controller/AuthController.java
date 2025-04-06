package com.victor_devv.todo_list.controller;

import com.victor_devv.todo_list.controller.advice.ApiResponseBuilder;
import com.victor_devv.todo_list.controller.advice.jsend.JSendResponse;
import com.victor_devv.todo_list.domain.dto.LoginRequest;
import com.victor_devv.todo_list.domain.dto.LoginResponse;
import com.victor_devv.todo_list.domain.dto.UserRequest;
import com.victor_devv.todo_list.service.UserService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth related endpoints")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Timed(value = "user.create", description = "Time taken to create a user")
    @Operation(summary = "Register a new user")
    public ResponseEntity<JSendResponse> create(@Valid @RequestBody UserRequest request) {
        return ApiResponseBuilder.success(userService.create(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Timed(value = "user.login", description = "Time taken to authenticate a user")
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<LoginResponse> authenticate(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
