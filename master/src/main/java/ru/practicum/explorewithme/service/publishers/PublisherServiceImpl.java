package ru.practicum.explorewithme.service.publishers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dao.UserRepository;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exception.PublisherNotFoundException;
import ru.practicum.explorewithme.exception.UserNotFoundException;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.service.event.EventService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final UserRepository userRepository;
    private final EventService eventService;

    @Override
    public List<UserDto> getPublishers(Long userId) {
        User user = findUser(userId);
        return user.getPublishers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getPublisherEvents(Long userId, Long publisherId, int from, int size) {
        User user = findUser(userId);
        User publisher = findUser(publisherId);
        if (!user.getPublishers().contains(publisher)) {
            throw new PublisherNotFoundException(userId, publisherId);
        }
        return eventService.getActualEvents(Collections.singletonList(publisherId), from, size);
    }

    @Override
    public List<EventShortDto> getAllPublisherEvents(Long userId, int from, int size) {
        User user = findUser(userId);
        if (user.getPublishers().isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> pulisherIds = user.getPublishers()
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return eventService.getActualEvents(pulisherIds, from, size);
    }

    @Override
    public void addPublisher(Long userId, Long publisherId) {
        User user = findUser(userId);
        User publisher = findUser(publisherId);
        user.getPublishers().add(publisher);
        userRepository.save(user);
    }

    @Override
    public void deletePublisher(Long userId, Long publisherId) {
        User user = findUser(userId);
        User publisher = findUser(publisherId);
        if (!user.getPublishers().contains(publisher)) {
            throw new PublisherNotFoundException(userId, publisherId);
        }
        user.getPublishers().remove(publisher);
        userRepository.save(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
    }
}
