package com.victor_devv.todo_list.service.impl;

import com.victor_devv.todo_list.config.jwt.JwtService;
import com.victor_devv.todo_list.domain.dto.*;
import com.victor_devv.todo_list.domain.entity.Role;
import com.victor_devv.todo_list.domain.entity.User;
import com.victor_devv.todo_list.domain.mapper.UserMapper;
import com.victor_devv.todo_list.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;
    private UserRequest userRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@victordevv.com")
                .password("hashedpass")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .username("johndoe")
                .email("john@victordevv.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        userRequest = UserRequest.builder()
                .username("johndoe")
                .email("john@victordevv.com")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .build();

        loginRequest = LoginRequest.builder()
                .email("john@victordevv.com")
                .password("password123")
                .build();
    }

    @Test
    void testFindById_whenUserExists_returnsUserDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.findById(1L);
        assertEquals("johndoe", result.getUsername());
    }

    @Test
    void testFindById_whenUserNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindAll_returnsPagedUsers() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        Page<UserDto> result = userService.findAll(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testCreate_success() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);
        when(userRepository.existsByEmail("john@victordevv.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedpass");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(jwtService.generateToken(anyMap(), any(User.class))).thenReturn("jwt-token");

        LoginResponse response = userService.create(userRequest);

        assertEquals("johndoe", response.getUser().getUsername());
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void testCreate_duplicateUsername_throwsException() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.create(userRequest));
    }

    @Test
    void testCreate_duplicateEmail_throwsException() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);
        when(userRepository.existsByEmail("john@victordevv.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.create(userRequest));
    }

    @Test
    void testCreate_emptyPassword_throwsException() {
        userRequest.setPassword("  ");
        assertThrows(IllegalArgumentException.class, () -> userService.create(userRequest));
    }

    @Test
    void testCreate_shortPassword_throwsException() {
        userRequest.setPassword("short");
        assertThrows(IllegalArgumentException.class, () -> userService.create(userRequest));
    }

    @Test
    void testAuthenticate_success() {
        when(userRepository.findByEmail("john@victordevv.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(jwtService.generateToken(anyMap(), eq(user))).thenReturn("jwt-token");

        LoginResponse response = userService.authenticate(loginRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void testAuthenticate_userNotFound_throwsException() {
        when(userRepository.findByEmail("john@victordevv.com")).thenReturn(Optional.empty());
        verify(authenticationManager, never()).authenticate(any());
        assertThrows(NoSuchElementException.class, () -> userService.authenticate(loginRequest));
    }
}
