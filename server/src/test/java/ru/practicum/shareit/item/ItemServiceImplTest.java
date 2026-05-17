package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemServiceImplTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void testGetOwnerItems() {
        var user1 = userService.createUser(makeUserDto("Ivan", "ivan@gmail.com"));
        var user2 = userService.createUser(makeUserDto("Dmitry", "dmitry@gmail.com"));

        var item1 = itemService.createItem(makeItemDto("Item1", "1", true), user1.getId());
        var item2 = itemService.createItem(makeItemDto("Item2", "12", true), user1.getId());
        var item3 = itemService.createItem(makeItemDto("Item3", "123", true), user1.getId());

        var booking1 = bookingService.createBooking(
                makeBookingDto(item1.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)),
                user2.getId()
        );
        bookingService.resolveBooking(user1.getId(), booking1.getId(), true);

        List<ResponseItemDto> userItems = itemService.getOwnerItems(user1.getId());

        assertThat(userItems.size(), equalTo(3));
        assertThat(userItems, containsInAnyOrder(
                hasProperty("name", equalTo(item1.getName())),
                hasProperty("name", equalTo(item2.getName())),
                hasProperty("name", equalTo(item3.getName()))
        ));

        var itemWithBooking = userItems.stream()
                .filter(i -> i.getId().equals(item1.getId()))
                .findFirst().orElseThrow();

        assertThat(itemWithBooking.getLastBooking(), nullValue());
        assertThat(itemWithBooking.getNextBooking(), notNullValue());
    }

    private ItemDto makeItemDto(String name, String description, boolean available) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        return itemDto;
    }

    private UserDto makeUserDto(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        return userDto;
    }

    private RequestBookingDto makeBookingDto(Long itemId, LocalDateTime start, LocalDateTime end) {
        RequestBookingDto bookingDto = new RequestBookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        return bookingDto;
    }
}
