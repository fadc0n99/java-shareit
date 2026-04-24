package ru.practicum.shareit.booking.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class EntityUtils {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User with %d not found", userId)));
    }

    public Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(
                        () -> new ItemNotFoundException(String.format("Item with %d not found", itemId)));
    }

    public Booking getBookingWithOwnerOrThrow(Long bookingId) {
        return bookingRepository.findWithOwnerById(bookingId)
                .orElseThrow(
                        () -> new BookingNotFoundException(String.format("Booking with %d not found", bookingId))
                );
    }

    public Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(
                        () -> new BookingNotFoundException(String.format("Booking with %d not found", bookingId))
                );
    }

    public void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("User with %d not found", userId));
        }
    }

    public void checkItemExists(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException(String.format("Item with %d not found", itemId));
        }
    }
}
