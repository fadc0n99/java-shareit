package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(CreateUserDto userDto) {
        validateExistsUserByEmail(userDto.getEmail());

        User user = UserMapper.toEntity(userDto);
        User newUser = userRepository.add(user);
        return UserMapper.toDto(newUser);
    }

    @Override
    public UserResponseDto updateUser(UpdateUserDto userDto, Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User with %d not found", userId)));

        if (userDto.getName() != null) {
            currentUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            validateDuplicateEmail(userDto.getEmail(), userId);
            currentUser.setEmail(userDto.getEmail());
        }

        User updatedUser = userRepository.update(currentUser);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User with %d not found", userId)));
        return UserMapper.toDto(currentUser);
    }

    private void validateExistsUserByEmail(String email) {
        boolean isSameEmail = userRepository.isExistsByEmail(email);
        if (isSameEmail) {
            throw new EmailConflictException(String.format("Email %s is already exists", email));
        }
    }

    private void validateDuplicateEmail(String email, Long userId) {
        boolean isDuplicate = userRepository.isDuplicateEmail(email, userId);
        if (isDuplicate) {
            throw new EmailConflictException(String.format("Email %s is already taken", email));
        }
    }
}
