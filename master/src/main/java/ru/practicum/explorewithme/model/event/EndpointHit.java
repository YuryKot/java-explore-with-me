package ru.practicum.explorewithme.model.event;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
public class EndpointHit {

    private Long id;

    private String app = "ExploreWithMe";
    private String uri;
    private String ip;
    private String timestamp;

    public EndpointHit(String uri, String ip) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.uri = uri;
        this.ip = ip;
        this.timestamp = formatter.format(LocalDateTime.now());
    }
}
