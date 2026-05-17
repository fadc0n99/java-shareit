package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserServiceImplTest {

    private final UserService userService;

    @Test
    void testUpdateUser() {
        UserDto user = userService.createUser(makeUserDto("abc", "abc@mail.ru"));
        userService.updateUser(makeUserDto(null, "abc1@mail.ru"), user.getId());

        UserDto result = userService.getUser(user.getId());

        assertThat(result.getName(), equalTo("abc"));
        assertThat(result.getEmail(), equalTo("abc1@mail.ru"));
    }

    private UserDto makeUserDto(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        return userDto;
    }
}
