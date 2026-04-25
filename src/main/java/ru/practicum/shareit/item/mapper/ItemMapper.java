package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static Item toEntity(RequestItemDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        return item;
    }

    public static ResponseItemDto toDto(Item item) {
        ResponseItemDto responseItemDto = new ResponseItemDto();
        responseItemDto.setId(item.getId());
        responseItemDto.setName(item.getName());
        responseItemDto.setDescription(item.getDescription());
        responseItemDto.setAvailable(item.getAvailable());
        responseItemDto.setComments(CommentMapper.toDtos(item.getComments()));
        return responseItemDto;
    }

    public static ResponseItemDto toOwnerDto(Item item, Booking last, Booking next) {
        ResponseItemDto responseItemDto = new ResponseItemDto();
        responseItemDto.setId(item.getId());
        responseItemDto.setName(item.getName());
        responseItemDto.setDescription(item.getDescription());
        responseItemDto.setAvailable(item.getAvailable());
        responseItemDto.setComments(CommentMapper.toDtos(item.getComments()));

        responseItemDto.setLastBooking(last != null ? BookingMapper.toInfoDto(last) : null);
        responseItemDto.setNextBooking(next != null ? BookingMapper.toInfoDto(next): null);

        return responseItemDto;
    }
}
