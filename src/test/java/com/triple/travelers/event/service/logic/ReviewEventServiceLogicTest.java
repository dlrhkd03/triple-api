package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.ReviewEventDao;
import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.dto.EventDto;
import com.triple.travelers.event.vo.Event;
import com.triple.travelers.event.vo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewEventServiceLogicTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ReviewEventDao reviewEventDao;

    @Autowired
    private ReviewEventServiceLogic reviewEventServiceLogic;

    private final Event requestADD1 = Event.builder()
            .type("REVIEW")
            .action("ADD")
            .review_id("test reviewId")
            .content("test")
            .photo_url("test photo")
            .user_id("test user1")
            .place_id("test placeId")
            .build();

    private final Event requestADD2 = Event.builder()
            .type("REVIEW")
            .action("ADD")
            .review_id("test reviewId")
            .content("test")
            .photo_url(null)
            .user_id("test user2")
            .place_id("test placeId")
            .build();

    private final Event requestMOD = Event.builder()
            .type("REVIEW")
            .action("MOD")
            .review_id("test reviewId")
            .content("test")
            .photo_url(null)
            .user_id("test user")
            .place_id("test placeId")
            .build();

    private final Event requestDEL = Event.builder()
            .type("REVIEW")
            .action("DELETE")
            .review_id("test reviewId")
            .content("test")
            .photo_url(null)
            .user_id("test user")
            .place_id("test placeId")
            .build();

    @Test
    void test() {
        // Given
        userDao.createUser(User.builder()
                .user_id("test user1")
                .point(0)
                .build());
        userDao.createUser(User.builder()
                .user_id("test user2")
                .point(0)
                .build());
        // When
        User beforeUser1 = userDao.selectUser(requestADD1.getUser_id());
        EventDto event1 = reviewEventServiceLogic.add(requestADD1);
        User afterUser1 = userDao.selectUser(requestADD1.getUser_id());

        User beforeUser2 = userDao.selectUser(requestADD2.getUser_id());
        EventDto event2 = reviewEventServiceLogic.add(requestADD2);
        User afterUser2 = userDao.selectUser(requestADD2.getUser_id());
        // Then
        assertEquals(event1.getBonus_point(), 1);
        assertEquals(event1.getContent_point(), 1);
        assertEquals(event1.getPhoto_point(), 1);
        assertEquals(event1.getBonus_point(), afterUser1.getPoint() - beforeUser1.getPoint());

        assertEquals(event2.getBonus_point(), 0);
        assertEquals(event2.getContent_point(), 1);
        assertEquals(event2.getPhoto_point(), 0);
        assertEquals(event2.getBonus_point(), afterUser1.getPoint() - beforeUser1.getPoint());
    }
}