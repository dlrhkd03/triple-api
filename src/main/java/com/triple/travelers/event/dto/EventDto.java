package com.triple.travelers.event.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EventDto {
    private String type;
    private String action;
    private String review_id;
    private String user_id;
    private String place_id;
}
