package ru.practicum.explorewithme.model.event;

import lombok.Data;
import ru.practicum.explorewithme.mapper.DateTimeMapper;

import java.time.LocalDateTime;


@Data
public class EndpointHit {

    private Long id;

    private String app = "ExploreWithMe";
    private String uri;
    private String ip;
    private String timestamp;

    public EndpointHit(String uri, String ip) {
        this.uri = uri;
        this.ip = ip;
        this.timestamp = DateTimeMapper.toString(LocalDateTime.now());
    }
}
