package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL,
    // Текущие
    CURRENT,
    // Будущие
    FUTURE,
    // Завершенные
    PAST,
    // Отклоненные
    REJECTED,
    // Ожидающие подтверждения
    WAITING
}
