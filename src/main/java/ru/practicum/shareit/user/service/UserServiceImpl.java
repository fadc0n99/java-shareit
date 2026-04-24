package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.utils.EntityUtils;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EntityUtils entityUtils;

    @Override
    @Transactional
    public UserResponseDto createUser(CreateUserDto userDto) {
        if (isExistsAnyUserByEmail(userDto.getEmail())) {
            throw new EmailConflictException(String.format("Email %s is already exists", userDto.getEmail()));
        }

        User user = UserMapper.toEntity(userDto);
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UpdateUserDto userDto, Long userId) {
        User currentUser = entityUtils.getUserOrThrow(userId);

        if (userDto.getName() != null) {
            currentUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (isExistsAnotherUserByEmail(userDto.getEmail(), currentUser.getId())) {
                throw new EmailConflictException(String.format("Email %s is already taken", userDto.getEmail()));
            }
            currentUser.setEmail(userDto.getEmail());
        }

        return UserMapper.toDto(currentUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        User currentUser = entityUtils.getUserOrThrow(userId);
        return UserMapper.toDto(currentUser);
    }

    private boolean isExistsAnyUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isExistsAnotherUserByEmail(String email, Long userId) {
        return userRepository.existsByEmailAndIdNot(email, userId);
    }
}

