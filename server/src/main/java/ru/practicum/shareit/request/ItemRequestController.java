package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ResponseRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ResponseRequestDto> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ResponseRequestDto dto) {
        return ResponseEntity.ok(itemRequestService.createRequest(dto, userId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ResponseRequestDto> getItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        return ResponseEntity.ok(itemRequestService.getRequestById(userId, requestId));
    }

    @GetMapping
    public ResponseEntity<List<ResponseRequestDto>> getUserItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemRequestService.getUserRequests(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseRequestDto>> getItemRequestsByOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemRequestService.getOtherUsersRequests(userId));
    }

}
