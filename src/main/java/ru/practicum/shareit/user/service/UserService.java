package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(CreateUserDto userDto);

    UserResponseDto updateUser(UpdateUserDto userDto, Long userId);

    void deleteUser(Long userId);

    UserResponseDto getUser(Long userId);
}
