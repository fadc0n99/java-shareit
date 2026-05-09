package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InfoBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {

    public static Booking toEntity(BookingDto bookingDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        return booking;
    }

    public static ResponseBookingDto toDto(Booking booking) {
        ResponseBookingDto dto = new ResponseBookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        dto.setBooker(UserMapper.toDto(booking.getBooker()));
        dto.setItem(ItemMapper.toShortDto(booking.getItem()));
        return dto;
    }

    public static InfoBookingDto toInfoDto(Booking booking) {
        InfoBookingDto infoBookingDto = new InfoBookingDto();
        infoBookingDto.setId(booking.getId());
        infoBookingDto.setStart(booking.getStart());
        infoBookingDto.setEnd(booking.getEnd());
        return infoBookingDto;
    }

    public static List<ResponseBookingDto> toDtos(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }
}
