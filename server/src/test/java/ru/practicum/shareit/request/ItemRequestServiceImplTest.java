package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemRequestServiceImplTest {

    private final ItemRequestService itemRequestService;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void testGetOtherUsersRequests() {
        var user1 = userService.createUser(makeUserDto("user1", "user1@mail.com"));
        var user2 = userService.createUser(makeUserDto("user2", "user2@mail.com"));

        itemRequestService.createRequest(makeRequestItem("123"), user1.getId());
        itemRequestService.createRequest(makeRequestItem("12345"), user1.getId());

        List<ItemRequestDto> itemRequestDtos = itemRequestService.getOtherUsersRequests(user2.getId());

        assertThat(itemRequestDtos.size(), equalTo(2));
    }

    private ItemRequestDto makeRequestItem(String description) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription(description);
        return dto;
    }

    private UserDto makeUserDto(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        return userDto;
    }
}
