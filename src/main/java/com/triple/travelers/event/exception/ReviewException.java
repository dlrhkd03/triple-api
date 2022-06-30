package com.triple.travelers.event.exception;

import lombok.Getter;

@Getter
public class ReviewException extends RuntimeException {
    private final ReviewErrorCode reviewErrorCode;
    private final String message;

    public ReviewException(ReviewErrorCode reviewErrorCode) {
        super(reviewErrorCode.getMessage());
        this.reviewErrorCode = reviewErrorCode;
        this.message = reviewErrorCode.getMessage();
    }
}
