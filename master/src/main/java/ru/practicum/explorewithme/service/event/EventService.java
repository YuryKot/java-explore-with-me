package ru.practicum.explorewithme.service.event;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.dto.event.AdminUpdateEventRequestDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.model.EventSort;

import java.util.List;

@Service
public interface EventService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, int from, int size);

    EventFullDto updateEvent(Long userId, UpdateEventRequestDto updateEventRequestDto);

    EventFullDto updateEvent(Long eventId, AdminUpdateEventRequestDto requestDto);

    EventFullDto getEvent(/*Long userId,*/ Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    List<EventShortDto> getEventsWithFilter(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from, Integer size);

    EventFullDto publishEvent(Long eventId);

    EventFullDto publishReject(Long eventId);

}
