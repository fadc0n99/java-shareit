package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> userMap;
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public User add(User user) {
        Long newId = idCounter.incrementAndGet();
        user.setId(newId);

        userMap.put(newId, user);
        return user;
    }

    @Override
    public User update(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(Long userId) {
        return userMap.remove(userId) != null;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public boolean isExists(Long userId) {
        return userMap.get(userId) != null;
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return userMap.values()
                .stream()
                .anyMatch(user -> Objects.equals(user.getEmail(), email));
    }

    @Override
    public boolean isDuplicateEmail(String email, Long userId) {
        return userMap.values()
                .stream()
                .anyMatch(user ->
                        !Objects.equals(user.getId(), userId) && Objects.equals(user.getEmail(), email)
                );
    }
}
