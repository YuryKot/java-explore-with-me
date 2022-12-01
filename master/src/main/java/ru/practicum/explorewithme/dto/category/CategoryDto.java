package ru.practicum.explorewithme.dto.category;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {

    @NotNull
    private Long id;

    @NotNull
    private String name;
}
