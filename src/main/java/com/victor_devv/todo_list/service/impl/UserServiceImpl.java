package com.victor_devv.todo_list.service.impl;

import com.victor_devv.todo_list.config.jwt.JwtService;
import com.victor_devv.todo_list.domain.dto.LoginRequest;
import com.victor_devv.todo_list.domain.dto.UserDto;
import com.victor_devv.todo_list.domain.dto.LoginResponse;
import com.victor_devv.todo_list.domain.dto.UserRequest;
import com.victor_devv.todo_list.domain.entity.Role;
import com.victor_devv.todo_list.domain.entity.User;
import com.victor_devv.todo_list.domain.mapper.UserMapper;
import com.victor_devv.todo_list.repository.UserRepository;
import com.victor_devv.todo_list.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("user with id: " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional
    public LoginResponse create(UserRequest userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("username already exists: " + userDto.getUsername());
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("email address already exists: " + userDto.getEmail());
        }

        if (userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("password cannot be empty");
        }

        if (userDto.getPassword().length() < 8) {
            throw new IllegalArgumentException("password must be at least 8 characters");
        }

        String hashedPassword = passwordEncoder.encode(userDto.getPassword());

        User user = User.builder()
                .username(userDto.getUsername())
                .password(hashedPassword)
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("created user with id: {}", savedUser.getId());

        var jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder()
                .user(userMapper.toDto(savedUser))
                .token(jwtToken)
                .build();
    }

    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(jwtToken)
                .build();
    }
}
