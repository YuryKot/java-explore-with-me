package ru.practicum.explorewithme.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;

@Data
public class EventShortDto implements EventDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    private String eventDate;

    private UserShortDto initiator;

    private boolean paid;

    private String title;

    private int views;
}
