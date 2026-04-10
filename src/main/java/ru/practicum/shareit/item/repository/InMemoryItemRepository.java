package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> itemMap;
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public Item add(Item item) {
        Long newId = idCounter.incrementAndGet();
        item.setId(newId);

        itemMap.put(newId, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findByItemId(Long itemId) {
        return Optional.ofNullable(itemMap.get(itemId));
    }

    @Override
    public List<Item> findOwnerItems(Long userId) {
        return itemMap.values()
                .stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .toList();
    }

    @Override
    public List<Item> searchBy(String text) {
        return itemMap.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    @Override
    public boolean isOwner(Long itemId, Long userId) {
        Item item = itemMap.get(itemId);
        return item != null && Objects.equals(item.getOwnerId(), userId);
    }
}
