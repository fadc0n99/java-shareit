package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.utils.EntityUtils;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
    private final EntityUtils entityUtils;

    @Override
    @Transactional
    public ResponseBookingDto createBooking(BookingDto bookingDto, Long userId) {
        checkBookingPeriod(bookingDto.getStart(), bookingDto.getEnd());

        User user = entityUtils.getUserOrThrow(userId);
        Item item = entityUtils.getItemOrThrow(bookingDto.getItemId());

        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new ValidationException("This item is currently unavailable");
        }

        Booking booking = BookingMapper.toEntity(bookingDto, user, item);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(savedBooking, user, item);
    }

    @Override
    @Transactional
    public ResponseBookingDto resolveBooking(Long userId, Long bookingId, boolean approved) {
        Booking currentBooking = entityUtils.getBookingWithOwnerOrThrow(bookingId);

        if (!isBookingItemOwner(currentBooking, userId)) {
            throw new ValidationException(String.format("User %s is not the owner", userId));
        }

        currentBooking.setStatus(approved ? APPROVED : REJECTED);

        return BookingMapper.toDto(currentBooking);
    }

    @Override
    public ResponseBookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = entityUtils.getBookingWithOwnerOrThrow(bookingId);

        boolean isBooker = booking.getBooker().getId().equals(userId);
        if (!isBooker && !isBookingItemOwner(booking, userId)) {
            throw new ValidationException("Only owner and booker have access to booking");
        }

        return BookingMapper.toDto(booking);
    }

    @Override
    public List<ResponseBookingDto> getUserBookings(Long userId, BookingState state) {
        List<Booking> userBookings = getBookingsByUserAndState(userId, state, false);

        return BookingMapper.toDtos(userBookings);
    }

    @Override
    public List<ResponseBookingDto> getOwnerBookings(Long ownerId, BookingState state) {
        List<Booking> ownerBookings = getBookingsByUserAndState(ownerId, state, true);

        return BookingMapper.toDtos(ownerBookings);
    }

    private List<Booking> getBookingsByUserAndState(Long userId, BookingState state, boolean isOwner) {
        entityUtils.checkUserExists(userId);
        BookingState effectiveState = state != null ? state : BookingState.ALL;

        return isOwner ?
                bookingRepository.findOwnerBookingsByState(userId, effectiveState.getState(), LocalDateTime.now()) :
                bookingRepository.findUserBookingsByState(userId, effectiveState.getState(), LocalDateTime.now());
    }

    private void checkBookingPeriod(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();

        if (start.isBefore(now) || end.isBefore(now)) {
            throw new ValidationException("Dates cannot be in past");
        }
        if (!start.isBefore(end)) {
            throw new ValidationException("Start date must be before end date");
        }
    }

    private boolean isBookingItemOwner(Booking booking, Long checkUserId) {
        Long userIdFromBooking = booking.getItem().getOwner().getId();

        return Objects.equals(userIdFromBooking, checkUserId);
    }

}
