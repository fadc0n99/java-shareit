package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ResponseRequestDto;

import java.util.List;

public interface ItemRequestService {

    ResponseRequestDto createRequest(ResponseRequestDto dto, Long userId);

    ResponseRequestDto getRequestById(Long userId, Long requestId);

    List<ResponseRequestDto> getUserRequests(Long userId);

    List<ResponseRequestDto> getOtherUsersRequests(Long userId);

    void addItemToRequest(Long requestId, Item item);
}
