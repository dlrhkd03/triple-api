package com.triple.travelers.event.dto;

import com.triple.travelers.event.exception.ReviewErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewErrorResponse {
    private ReviewErrorCode reviewErrorCode;
    private String message;


}
