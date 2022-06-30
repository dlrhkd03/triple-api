package com.triple.travelers.event.service;

import com.triple.travelers.event.vo.Event;

public interface ReviewEventService {
    Event add(com.triple.travelers.event.vo.Event request);

    Event mod(com.triple.travelers.event.vo.Event request);

    Event delete(com.triple.travelers.event.vo.Event request);
}
