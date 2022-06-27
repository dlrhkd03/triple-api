package com.triple.travelers.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    private int id;
    private String type;
    private String action;
    private String review_id;
    private String content;
    private String attachedPhotoIds;
    private String user_id;
    private String place_id;
}
