package com.triple.travelers.event.service;

import com.triple.travelers.event.dto.EventDto;
import com.triple.travelers.event.vo.Event;

public interface ReviewEventService {
    Event add(EventDto request);

    Event mod(EventDto request);

    Event delete(EventDto request);
}
