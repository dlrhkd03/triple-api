package com.triple.travelers.event.dto;

import com.triple.travelers.event.exception.EventErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventErrorResponse {
    private EventErrorCode eventErrorCode;
    private String message;
}
