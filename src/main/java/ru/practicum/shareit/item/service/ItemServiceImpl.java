package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.utils.EntityUtils;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
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
    public List<ResponseItemDto> getOwnerItems(Long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);

        return items.stream()
                .map(ItemMapper::toDto)
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
