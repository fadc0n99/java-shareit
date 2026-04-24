package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.InfoBookingDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseOwnerItemDto extends ResponseItemDto {

    private InfoBookingDto nextBooking;
    private InfoBookingDto lastBooking;
}
