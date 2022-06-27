package com.triple.travelers.event.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {

    private String user_id;
    private int point;
}
