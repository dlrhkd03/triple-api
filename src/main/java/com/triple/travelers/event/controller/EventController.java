package com.triple.travelers.event.controller;

import com.triple.travelers.event.dto.EventDto;
import com.triple.travelers.event.service.logic.ReviewEventServiceLogic;
import com.triple.travelers.event.service.logic.UserServiceLogic;
import com.triple.travelers.event.vo.Event;
import com.triple.travelers.event.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final UserServiceLogic userService;
    private final ReviewEventServiceLogic reviewEventService;

    @GetMapping("/user/{userId}")
    public User getPoint(
            @PathVariable String userId
    ) {
        log.info("get point user:{}", userId);
        return userService.getUser(userId);
    }

    @PostMapping("/events")
    public EventDto OccurredEvent(
            @RequestBody Event request
    ) {
        log.info("event occur type:{} action:{}", request.getType(), request.getAction());

        switch (request.getType()) {
            case "REVIEW":
                return responseReviewEvent(request);
            default:
                //없는 이벤트 요청시 런타임 에러
                throw new RuntimeException();
        }
    }

    public EventDto responseReviewEvent(Event request) {
        switch (request.getAction()) {
            case "ADD":
                return reviewEventService.add(request);
            case "MOD":
                return reviewEventService.mod(request);
            case "DELETE":
                return reviewEventService.delete(request);
            default:
                //없는 액션 요청시 런타임 에러
                throw new RuntimeException();
        }
    }
}
