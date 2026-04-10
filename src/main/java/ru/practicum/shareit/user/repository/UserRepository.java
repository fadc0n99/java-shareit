package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User add(User user);

    User update(User user);

    boolean delete(Long userId);

    Optional<User> findById(Long userId);

    boolean isExists(Long userId);

    boolean isExistsByEmail(String email);

    boolean isDuplicateEmail(String email, Long userId);
}
