package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.REJECTED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseBookingDto createBooking(RequestBookingDto bookingDto, Long userId) {
        checkBookingPeriod(bookingDto.getStart(), bookingDto.getEnd());

        User user = userRepository.findByIdOrThrow(userId);
        Item item = itemRepository.findByIdOrThrow(bookingDto.getItemId());

        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new ValidationException("This item is currently unavailable");
        }

        Booking booking = BookingMapper.toEntity(bookingDto, user, item);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public ResponseBookingDto resolveBooking(Long userId, Long bookingId, boolean approved) {
        Booking currentBooking = bookingRepository.findWithRelationsOrThrow(bookingId);

        if (!isBookingItemOwner(currentBooking, userId)) {
            throw new ValidationException(String.format("User %s is not the owner", userId));
        }

        currentBooking.setStatus(approved ? APPROVED : REJECTED);

        return BookingMapper.toDto(currentBooking);
    }

    @Override
    public ResponseBookingDto getBooking(Long userId, Long bookingId) {
        userRepository.findByIdOrThrow(userId);
        Booking booking = bookingRepository.findWithRelationsOrThrow(bookingId);

        boolean isBooker = booking.getBooker().getId().equals(userId);
        if (!isBooker && !isBookingItemOwner(booking, userId)) {
            throw new ValidationException("Only owner and booker have access to booking");
        }

        return BookingMapper.toDto(booking);
    }

    @Override
    public List<ResponseBookingDto> getUserBookings(Long userId, String state) {
        return getUserBookings(userId, state, false);
    }

    @Override
    public List<ResponseBookingDto> getOwnerBookings(Long ownerId, String state) {
        return getUserBookings(ownerId, state, true);
    }

    private List<ResponseBookingDto> getUserBookings(Long userId, String state, boolean isOwner) {
        userRepository.findByIdOrThrow(userId);

        List<Booking> userBookings = isOwner ?
                bookingRepository.findOwnerBookingsByState(userId, state) :
                bookingRepository.findUserBookingsByState(userId, state);

        return BookingMapper.toDtos(userBookings);
    }

    private void checkBookingPeriod(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new ValidationException("Start date must be before end date");
        }
    }

    private boolean isBookingItemOwner(Booking booking, Long checkUserId) {
        User bookingOwner = booking.getItem().getOwner();

        return Objects.equals(bookingOwner.getId(), checkUserId);
    }

}
