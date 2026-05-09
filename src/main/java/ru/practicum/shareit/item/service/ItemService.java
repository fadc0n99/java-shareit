package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;

import java.util.List;

public interface ItemService {

    ResponseItemDto createItem(ItemDto itemDto, Long userId);

    ResponseItemDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    ResponseItemDto getItemById(Long itemId);

    List<ResponseItemDto> getOwnerItems(Long userId);

    List<ResponseItemDto> searchAvailableItems(String text);

    CommentDto createComment(Long userId, Long itemId, CommentDto dto);
}
