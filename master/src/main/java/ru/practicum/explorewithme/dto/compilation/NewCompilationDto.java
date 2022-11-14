package ru.practicum.explorewithme.dto.compilation;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned = false;

    @NotNull
    private String title;
}
