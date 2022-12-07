package ru.practicum.explorewithme.service.publishers;

import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;

public interface PublisherService {

    List<UserDto> getPublishers(Long userId);

    List<EventShortDto> getPublisherEvents(Long userId, Long publisherId, int from, int size);

    void addPublisher(Long userId, Long publisherId);

    void deletePublisher(Long userId, Long publisherId);

    List<EventShortDto> getAllPublisherEvents(Long userId, int from, int size);
}
