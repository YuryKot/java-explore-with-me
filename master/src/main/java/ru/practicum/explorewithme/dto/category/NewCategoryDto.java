package ru.practicum.explorewithme.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewCategoryDto {

    @NotNull
    @NotBlank
    private String name;
}
