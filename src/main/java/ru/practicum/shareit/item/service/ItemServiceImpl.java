package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.utils.EntityUtils;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.ResponseOwnerItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final EntityUtils entityUtils;

    @Override
    @Transactional
    public ResponseItemDto createItem(RequestItemDto itemDto, Long userId) {
        User owner = entityUtils.getUserOrThrow(userId);

        Item item = ItemMapper.toEntity(itemDto, owner);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ResponseItemDto updateItem(RequestItemDto itemDto, Long itemId, Long userId) {
        User owner = entityUtils.getUserOrThrow(userId);
        Item currentItem = entityUtils.getItemOrThrow(itemId);

        if (!currentItem.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Only the owner can edit item");
        }

        if (itemDto.getName() != null) {
            currentItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            currentItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            currentItem.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toDto(currentItem);
    }

    @Override
    public ResponseItemDto getItemById(Long itemId) {
        Item currentItem = entityUtils.getItemOrThrow(itemId);

        return ItemMapper.toDto(currentItem);
    }

    @Override
    public List<ResponseOwnerItemDto> getOwnerItems(Long userId) {
        entityUtils.checkUserExists(userId);

        List<Item> items = itemRepository.findByOwnerId(userId);
        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream().map(Item::getId).toList();

        Map<Long, Booking> lastBookings = bookingRepository.findLastItemsBooking(itemIds)
                .stream()
                .collect(Collectors.toMap(
                        booking -> booking.getItem().getId(),
                        Function.identity()
                ));
        Map<Long, Booking> nextBookings = bookingRepository.findNextItemsBooking(itemIds)
                .stream()
                .collect(Collectors.toMap(
                        booking -> booking.getItem().getId(),
                        Function.identity()
                ));

        return items.stream()
                .map(item -> ItemMapper.toOwnerDto(
                        item,
                        lastBookings.get(item.getId()),
                        nextBookings.get(item.getId()))
                )
                .toList();
    }

    @Override
    public List<ResponseItemDto> searchAvailableItems(String text) {
        List<Item> items = itemRepository.searchAvailableItemsByText(text);

        return items.stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
