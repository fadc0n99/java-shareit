package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ResponseRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {

    public static ResponseRequestDto toDto(ItemRequest itemRequest) {
        List<ShortItemDto> shortItemDtos = itemRequest.getItems().stream().map(ItemMapper::toShortDto).toList();

        ResponseRequestDto responseRequestDto = new ResponseRequestDto();
        responseRequestDto.setId(itemRequest.getId());
        responseRequestDto.setDescription(itemRequest.getDescription());
        responseRequestDto.setCreated(itemRequest.getCreated());
        responseRequestDto.setItems(shortItemDtos);
        return responseRequestDto;
    }

    public static ItemRequest toEntity(ResponseRequestDto dto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setUser(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }
}
