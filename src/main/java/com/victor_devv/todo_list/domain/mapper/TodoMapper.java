package com.victor_devv.todo_list.domain.mapper;

import com.victor_devv.todo_list.domain.dto.TodoDto;
import com.victor_devv.todo_list.domain.dto.TodoRequest;
import com.victor_devv.todo_list.domain.entity.Todo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TodoMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    TodoDto toDto(Todo entity);

    List<TodoDto> toDtoList(List<Todo> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "priority", expression = "java(request.getPriority() != null ? Todo.Priority.valueOf(request.getPriority()) : null)")
    @Mapping(target = "status", expression = "java(request.getStatus() != null ? Todo.Status.valueOf(request.getStatus()) : null)")
    Todo toEntity(TodoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "priority", expression = "java(request.getPriority() != null ? Todo.Priority.valueOf(request.getPriority()) : entity.getPriority())")
    @Mapping(target = "status", expression = "java(request.getStatus() != null ? Todo.Status.valueOf(request.getStatus()) : entity.getStatus())")
    void updateEntityFromRequest(TodoRequest request, @MappingTarget Todo entity);
}
