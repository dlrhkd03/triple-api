package com.triple.travelers.event.exception;

import lombok.Getter;

@Getter
public class EventException extends RuntimeException {
    private final EventErrorCode eventErrorCode;
    private final String message;

    public EventException(EventErrorCode eventErrorCode) {
        super(eventErrorCode.getMessage());
        this.eventErrorCode = eventErrorCode;
        this.message = eventErrorCode.getMessage();
    }
}
