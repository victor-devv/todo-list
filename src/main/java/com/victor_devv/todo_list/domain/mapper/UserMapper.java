package com.victor_devv.todo_list.domain.mapper;

import com.victor_devv.todo_list.domain.dto.UserDto;
import com.victor_devv.todo_list.domain.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDto toDto(User entity);

    List<UserDto> toDtoList(List<User> entities);
}
