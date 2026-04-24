package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    ResponseBookingDto createBooking(BookingDto bookingDto, Long userId);

    ResponseBookingDto resolveBooking(Long userId, Long bookingId, boolean approved);

    ResponseBookingDto getBooking(Long userId, Long bookingId);

    List<ResponseBookingDto> getUserBookings(Long userId, BookingState state);

    List<ResponseBookingDto> getOwnerBookings(Long ownerId, BookingState state);
}
