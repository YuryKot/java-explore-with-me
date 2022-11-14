package ru.practicum.explorewithme.service.user;

import ru.practicum.explorewithme.dto.user.NewUserRequestDto;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto addUser(NewUserRequestDto newUserRequestDto);

    void deleteUser(Long userId);
}
