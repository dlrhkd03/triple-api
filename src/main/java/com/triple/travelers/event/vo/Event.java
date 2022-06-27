package com.triple.travelers.event.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    private int id;
    private String type;
    private String action;
    private String review_id;
    private String content;
    private String photo_url;
    private String user_id;
    private String place_id;
    private int bonus_point;
    private int content_point;
    private int photo_point;
    private LocalDateTime created_at;
}
