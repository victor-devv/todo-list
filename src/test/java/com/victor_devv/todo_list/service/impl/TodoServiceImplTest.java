package com.victor_devv.todo_list.service.impl;

import com.victor_devv.todo_list.domain.dto.TodoDto;
import com.victor_devv.todo_list.domain.dto.TodoRequest;
import com.victor_devv.todo_list.domain.entity.Todo;
import com.victor_devv.todo_list.domain.entity.User;
import com.victor_devv.todo_list.domain.mapper.TodoMapper;
import com.victor_devv.todo_list.repository.TodoRepository;
import com.victor_devv.todo_list.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    private AutoCloseable closeable;

    private User mockUser;
    private Todo mockTodo;
    private TodoDto mockTodoDto;
    private TodoRequest mockRequest;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("john@victordevv.com");

        mockTodo = new Todo();
        mockTodo.setId(1L);
        mockTodo.setTitle("Test");
        mockTodo.setUser(mockUser);

        mockTodoDto = new TodoDto();
        mockTodoDto.setId(1L);
        mockTodoDto.setTitle("Test");

        mockRequest = new TodoRequest();
        mockRequest.setTitle("New Task");
    }

    @Test
    void findById_shouldReturnTodoDto_whenFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        when(todoMapper.toDto(mockTodo)).thenReturn(mockTodoDto);

        TodoDto result = todoService.findById(1L);

        assertThat(result).isEqualTo(mockTodoDto);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.findById(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAllByUserId_shouldReturnPage_whenUserExists() {
        Page<Todo> todoPage = new PageImpl<>(List.of(mockTodo));
        Page<TodoDto> dtoPage = new PageImpl<>(List.of(mockTodoDto));
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(todoRepository.findByUserId(1L, pageable)).thenReturn(todoPage);
        when(todoMapper.toDto(mockTodo)).thenReturn(mockTodoDto);

        Page<TodoDto> result = todoService.findAllByUserId(1L, pageable);

        assertThat(result.getContent()).containsExactly(mockTodoDto);
    }

    @Test
    void findAllByUserId_shouldThrowException_whenUserDoesNotExist() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> todoService.findAllByUserId(1L, pageable))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_shouldReturnTodoDto_whenSuccessful() {
        when(userRepository.findByEmail("john@victordevv.com")).thenReturn(Optional.of(mockUser));
        when(todoMapper.toEntity(mockRequest)).thenReturn(mockTodo);
        when(todoRepository.save(mockTodo)).thenReturn(mockTodo);
        when(todoMapper.toDto(mockTodo)).thenReturn(mockTodoDto);

        TodoDto result = todoService.create("john@victordevv.com", mockRequest);

        assertThat(result).isEqualTo(mockTodoDto);
    }

    @Test
    void create_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByEmail("jane@victordevv.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.create("jane@victordevv.com", mockRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_shouldReturnUpdatedTodoDto() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        doAnswer(invocation -> {
            TodoRequest req = invocation.getArgument(0);
            Todo entity = invocation.getArgument(1);
            entity.setTitle(req.getTitle());
            return null;
        }).when(todoMapper).updateEntityFromRequest(mockRequest, mockTodo);
        when(todoRepository.save(mockTodo)).thenReturn(mockTodo);
        when(todoMapper.toDto(mockTodo)).thenReturn(mockTodoDto);

        TodoDto result = todoService.update(1L, mockRequest);

        assertThat(result).isEqualTo(mockTodoDto);
    }

    @Test
    void update_shouldThrowException_whenTodoNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.update(1L, mockRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_shouldDeleteTodo_whenExists() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));

        todoService.delete(1L);

        verify(todoRepository).delete(mockTodo);
    }

    @Test
    void delete_shouldThrowException_whenTodoNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.delete(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void markAsCompleted_shouldUpdateStatusAndReturnDto() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        when(todoRepository.save(mockTodo)).thenReturn(mockTodo);
        when(todoMapper.toDto(mockTodo)).thenReturn(mockTodoDto);

        TodoDto result = todoService.markAsCompleted(1L);

        assertThat(mockTodo.getStatus()).isEqualTo(Todo.Status.COMPLETED);
        assertThat(mockTodo.getCompletedAt()).isNotNull();
        assertThat(result).isEqualTo(mockTodoDto);
    }

    @Test
    void markAsCompleted_shouldThrowException_whenTodoNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.markAsCompleted(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
