package ru.practicum.explorewithme.dto.event;


public interface UpdateEventDto {

    String getAnnotation();

    Long getCategory();

    String getDescription();

    String getEventDate();

    Boolean getPaid();

    Integer getParticipantLimit();

    Boolean getRequestModeration();

    String getTitle();

}
