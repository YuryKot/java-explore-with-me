package ru.practicum.explorewithme.service.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dao.EventRepository;
import ru.practicum.explorewithme.dao.RequestRepository;
import ru.practicum.explorewithme.dao.UserRepository;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.mapper.RequestMapper;
import ru.practicum.explorewithme.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        return requestRepository.findByEvent_Id(eventId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFoundExceprion(reqId));
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new UpdateImpossibleException("Confirmation is not required");
        }
        List<Request> requests = requestRepository.findByEvent_IdAndStatus(eventId, StatusRequest.CONFIRMED);
        if (requests.size() >= event.getParticipantLimit()) {
            throw new UpdateImpossibleException("Limit has been reached");
        }
        request.setStatus(StatusRequest.CONFIRMED);
        if (requests.size() + 1 >= event.getParticipantLimit()) {
            requestRepository.findByEvent_IdAndStatus(eventId, StatusRequest.PENDING)
                    .forEach(r -> r.setStatus(StatusRequest.REJECTED));
        }
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFoundExceprion(reqId));
        request.setStatus(StatusRequest.REJECTED);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        return requestRepository.findByRequester_Id(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        Request existRequest = requestRepository.findByRequester_IdAndEvent_Id(userId, eventId);
        if (existRequest != null) {
            throw new RequestParamException(String.format("Request with userId=%d, eventId=%d already exist", userId, eventId));
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestParamException(String.format("User with id=%d is initiator event with id=%d", userId, eventId));
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new RequestParamException("Event is not publish");
        }
        int count = requestRepository.findByEvent_IdAndStatus(eventId, StatusRequest.CONFIRMED).size();
        if (count >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new RequestParamException("Limit participant has been reached");
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);
        request.setEvent(event);
        if (!event.isRequestModeration()) {
            request.setStatus(StatusRequest.CONFIRMED);
        } else {
            request.setStatus(StatusRequest.PENDING);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundExceprion(requestId));
        if (request.getRequester().getId() != userId) {
            throw new RequestParamException(String.format("User with id=%d is not initiator request with id=%d", userId, requestId));
        }
        request.setStatus(StatusRequest.REJECTED);
        return RequestMapper.toParticipationRequestDto(request);
    }
}
