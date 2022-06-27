package com.triple.travelers.event.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    private String review_id;
    private String user_id;
    private String place_id;
}
