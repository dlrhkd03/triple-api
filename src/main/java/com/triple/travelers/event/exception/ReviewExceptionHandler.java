package com.triple.travelers.event.exception;

import com.triple.travelers.event.dto.ReviewErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ReviewExceptionHandler {

    @ExceptionHandler(ReviewException.class)
    public ReviewErrorResponse handleExceptions(ReviewException e, HttpServletRequest request) {
        log.info("errorCode:{}, url:{}, message:{}", e.getReviewErrorCode(), request.getRequestURI(), e.getMessage());
        return new ReviewErrorResponse(e.getReviewErrorCode(), e.getMessage());
    }
}
