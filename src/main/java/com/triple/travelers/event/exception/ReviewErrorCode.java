package com.triple.travelers.event.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReviewErrorCode {

    NO_REVIEW("해당되는 리뷰가 없습니다."),
    USER_ID_REVIEW_ID_NOT_MATCHED("유저와 리뷰가 맞지 않습니다.");

    private final String message;
}
