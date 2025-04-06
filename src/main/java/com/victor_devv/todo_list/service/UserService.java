package com.victor_devv.todo_list.service;

import com.victor_devv.todo_list.domain.dto.LoginRequest;
import com.victor_devv.todo_list.domain.dto.UserDto;
import com.victor_devv.todo_list.domain.dto.LoginResponse;
import com.victor_devv.todo_list.domain.dto.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDto findById(Long id);

    Page<UserDto> findAll(Pageable pageable);

    LoginResponse create(UserRequest userDto);

    LoginResponse authenticate(LoginRequest request);

}
