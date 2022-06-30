package com.triple.travelers.event.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventErrorCode {

    NO_USER("해당되는 유저가 없습니다."),
    NO_TYPE("해당되는 이벤트가 없습니다."),
    NO_ACTION("해당되는 액션이 없습니다."),

    INTERNAL_SERVER_ERROR("서버 내부의 오류가 발생하였습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;
}
