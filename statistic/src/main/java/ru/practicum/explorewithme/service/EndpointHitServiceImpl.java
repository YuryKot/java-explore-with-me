package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dao.EndpointHitRepository;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.mapper.EndpointHitMapper;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime;
        if (start != null) {
            startDateTime = LocalDateTime.parse(start, formatter);
        } else {
            startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
        }
        LocalDateTime endDateTime;
        if (end != null) {
            endDateTime = LocalDateTime.parse(end, formatter);
        } else {
            endDateTime = LocalDateTime.of(2999, 1, 1, 0, 0);
        }
        List<ViewStats> endpointHits;
        if (uris == null) {
            if (unique) {
                endpointHits = endpointHitRepository.getStatsByStartAndStop(startDateTime, endDateTime);
            } else {
                endpointHits = endpointHitRepository.getStatsByStartAndStopUniqIp(startDateTime, endDateTime);
            }
        } else {
            if (unique) {
                endpointHits = endpointHitRepository.getStatsByUrisAndStartAndStopUniqIp(
                        uris, startDateTime, endDateTime
                );
            } else {
                endpointHits = endpointHitRepository.getStatsByUrisAndStartAndStop(
                        uris, startDateTime, endDateTime
                );
            }
        }
        return endpointHits;
    }
}
