package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        List<ShortItemDto> shortItemDtos = itemRequest.getItems().stream().map(ItemMapper::toShortDto).toList();

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(shortItemDtos);
        return itemRequestDto;
    }

    public static ItemRequest toEntity(ItemRequestDto dto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setUser(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }
}
