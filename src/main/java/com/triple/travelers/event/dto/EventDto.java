package com.triple.travelers.event.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@Setter
@NoArgsConstructor
public class EventDto {
    String type;
    String action;
    String reviewId;
    String content;
    String[] attachedPhotoIds;
    String userId;
    String placeId;
}
