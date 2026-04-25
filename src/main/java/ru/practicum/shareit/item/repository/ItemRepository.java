package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @EntityGraph(attributePaths = {"comments", "owner"})
    List<Item> findByOwnerId(long userId);

    @EntityGraph(attributePaths = {"comments", "owner"})
    Optional<Item> findWithCommentsById(long id);

    @Query("SELECT i FROM Item i WHERE " +
            "i.available = true AND " +
            "(LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> searchAvailableItemsByText(@Param("text") String text);
}
