package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseItemDto createItem(RequestItemDto itemDto, Long userId) {
        if (!userRepository.isExists(userId)) {
            throw new UserNotFoundException(String.format("User with %d not found", userId));
        }

        Item item = ItemMapper.toEntity(itemDto, userId);
        Item newItem = itemRepository.add(item);
        return ItemMapper.toDto(newItem);
    }

    @Override
    public ResponseItemDto updateItem(RequestItemDto itemDto, Long itemId, Long userId) {
        if (!userRepository.isExists(userId)) {
            throw new UserNotFoundException(String.format("User with %d not found", userId));
        }

        Item currentItem = itemRepository.findByItemId(itemId)
                .orElseThrow(
                        () -> new ItemNotFoundException(String.format("Item with %d not found", itemId)));

        if (!itemRepository.isOwner(itemId, userId)) {
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

        Item updatedItem = itemRepository.update(currentItem);
        return ItemMapper.toDto(updatedItem);
    }

    @Override
    public ResponseItemDto getItemById(Long itemId) {
        Item currentItem = itemRepository.findByItemId(itemId)
                .orElseThrow(
                        () -> new ItemNotFoundException(String.format("Item with %d not found", itemId)));
        return ItemMapper.toDto(currentItem);
    }

    @Override
    public List<ResponseItemDto> getOwnerItems(Long userId) {
        List<Item> items = itemRepository.findOwnerItems(userId);

        return items.stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public List<ResponseItemDto> searchAvailableItems(String text) {
        List<Item> items = itemRepository.searchBy(text);

        return items.stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
