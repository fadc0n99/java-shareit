package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookingState {
    ALL("ALL"),
    CURRENT("CURRENT"),
    PAST("PAST"),
    FUTURE("FUTURE"),
    WAITING("WAITING"),
    REJECTED("REJECTED");

    private final String state;
}
