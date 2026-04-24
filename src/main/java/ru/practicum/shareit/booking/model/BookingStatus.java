package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookingStatus {
    WAITING("waiting"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String status;
}
