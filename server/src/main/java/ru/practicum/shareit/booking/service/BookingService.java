package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.List;

public interface BookingService {

    ResponseBookingDto createBooking(RequestBookingDto bookingDto, Long userId);

    ResponseBookingDto resolveBooking(Long userId, Long bookingId, boolean approved);

    ResponseBookingDto getBooking(Long userId, Long bookingId);

    List<ResponseBookingDto> getUserBookings(Long userId, String state, boolean isOwner);
}
