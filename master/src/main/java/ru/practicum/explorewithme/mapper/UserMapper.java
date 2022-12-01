package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.user.NewUserRequestDto;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.model.User;

public class UserMapper {

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser(NewUserRequestDto newUserRequestDto) {
        User user = new User();
        user.setName(newUserRequestDto.getName());
        user.setEmail(newUserRequestDto.getEmail());
        return user;
    }
}
