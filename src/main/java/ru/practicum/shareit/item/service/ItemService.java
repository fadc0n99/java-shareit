package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.ResponseOwnerItemDto;

import java.util.List;

public interface ItemService {

    ResponseItemDto createItem(RequestItemDto itemDto, Long userId);

    ResponseItemDto updateItem(RequestItemDto itemDto, Long itemId, Long userId);

    ResponseItemDto getItemById(Long itemId);

    List<ResponseOwnerItemDto> getOwnerItems(Long userId);

    List<ResponseItemDto> searchAvailableItems(String text);
}
