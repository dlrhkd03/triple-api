package com.triple.travelers.event.exception;

import com.triple.travelers.event.dto.EventErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class EventExceptionHandler {

    @ExceptionHandler(EventException.class)
    public EventErrorResponse handleExceptions(EventException e, HttpServletRequest request) {
        log.info("errorCode:{}, url:{}, message:{}", e.getEventErrorCode(), request.getRequestURI(), e.getMessage());
        return new EventErrorResponse(e.getEventErrorCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public EventErrorResponse handleExceptions(Exception e, HttpServletRequest request) {
        log.info("url:{}, message:{}", request.getRequestURI(), e.getMessage());
        return new EventErrorResponse(EventErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
