package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateItemDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
}
