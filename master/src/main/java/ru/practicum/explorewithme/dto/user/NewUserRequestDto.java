package ru.practicum.explorewithme.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class NewUserRequestDto {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;
}
