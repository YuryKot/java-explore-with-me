package ru.practicum.explorewithme.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    float lat;
    float lon;
}
