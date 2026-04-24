package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Optional<Booking> findWithOwnerById(long bookingId);

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.booker bkr
            JOIN FETCH b.item i
            WHERE i.owner.id = :ownerId
            AND (:state = 'ALL'
                OR (:state = 'CURRENT' AND b.start <= :now AND b.end >= :now)
                OR (:state = 'PAST' AND b.end < :now)
                OR (:state = 'FUTURE' AND b.start > :now)
                OR (:state = 'WAITING' AND b.status = 'WAITING')
                OR (:state = 'REJECTED' AND b.status = 'REJECTED')
            )
            ORDER BY b.start DESC
            """)
    List<Booking> findOwnerBookingsByState(Long ownerId, String state, LocalDateTime now);

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.booker bkr
            JOIN FETCH b.item i
            WHERE bkr.id = :bookerId
            AND (:state = 'ALL'
                OR (:state = 'CURRENT' AND b.start <= :now AND b.end >= :now)
                OR (:state = 'PAST' AND b.end < :now)
                OR (:state = 'FUTURE' AND b.start > :now)
                OR (:state = 'WAITING' AND b.status = 'WAITING')
                OR (:state = 'REJECTED' AND b.status = 'REJECTED')
            )
            ORDER BY b.start DESC
            """)
    List<Booking> findUserBookingsByState(Long bookerId, String state, LocalDateTime now);
}
