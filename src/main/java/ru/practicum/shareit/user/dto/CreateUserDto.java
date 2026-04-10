package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserDto {
    @NotBlank
    @NotNull
    private String name;

    @Email
    @NotBlank
    @NotNull
    private String email;
}


