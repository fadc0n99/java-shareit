package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(UserDto userDto);

    UserResponseDto updateUser(UserDto userDto, Long userId);

    void deleteUser(Long userId);

    UserResponseDto getUser(Long userId);
}
