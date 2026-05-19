package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.InfoBookingDto;

import java.util.List;

@Data
public class ResponseItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private InfoBookingDto nextBooking;
    private InfoBookingDto lastBooking;
    private List<CommentDto> comments;
}