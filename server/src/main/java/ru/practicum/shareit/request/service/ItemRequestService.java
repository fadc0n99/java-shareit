package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createRequest(ItemRequestDto dto, Long userId);

    ItemRequestDto getRequestById(Long userId, Long requestId);

    List<ItemRequestDto> getUserRequests(Long userId);

    List<ItemRequestDto> getOtherUsersRequests(Long userId);

    void addItemToRequest(Long requestId, Item item);
}
