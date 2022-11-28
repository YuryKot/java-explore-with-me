package ru.practicum.explorewithme.dto.event;

public interface EventDto {
    Long getId();

    void setViews(int views);

    void setConfirmedRequests(int confirmedRequests);

}
