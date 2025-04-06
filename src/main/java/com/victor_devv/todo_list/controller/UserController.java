package com.victor_devv.todo_list.controller;

import com.victor_devv.todo_list.domain.dto.UserDto;
import com.victor_devv.todo_list.service.UserService;
import com.victor_devv.todo_list.util.Constants;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User related endpoints")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Timed(value = "user.findById", description = "Time taken to find a user by id")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    @Timed(value = "user.findAll", description = "Time taken to find all users")
    public ResponseEntity<Page<UserDto>> findAll(
            @PageableDefault(size = Constants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

}
