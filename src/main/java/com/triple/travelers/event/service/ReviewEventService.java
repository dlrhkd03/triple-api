package com.triple.travelers.event.service;

import com.triple.travelers.event.dto.EventDto;
import com.triple.travelers.event.vo.Event;

public interface ReviewEventService {
    EventDto add(Event request);

    EventDto mod(Event request);

    EventDto delete(Event request);
}
