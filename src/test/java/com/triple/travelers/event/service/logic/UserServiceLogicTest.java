package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.vo.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceLogicTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserServiceLogic userService;

    private final String defaultId = "a62bac06-86ba-4ab6-b467-a01b60e6b66a";
    private final int defaultPoint = 10;
    private final User defaultUser = User.builder()
            .user_id(defaultId)
            .point(defaultPoint)
            .build();

    /*@AfterEach
    void after() {
        userService.deleteUser(defaultId);
    }*/

    @DisplayName("디폴트 유저 생성")
    @Test
    void creatTest() {
        //when
        userService.createUser(defaultUser);
        User user = userService.getUser(defaultId);
        //then
        assertEquals(user.getUser_id(), defaultId);
        assertEquals(user.getPoint(), defaultPoint);
    }

    @DisplayName("유저 포인트 조회")
    @Test
    void retrieveTest() {
        // Given

        // When
        User user = userService.getUser(defaultId);
        // Then
        assertEquals(user.getUser_id(), defaultId);
        assertEquals(user.getPoint(), defaultPoint);
    }

    @DisplayName("유저 포인트 변경")
    @Test
    void updatePointTest() {
        // Given
        User user = userService.getUser(defaultId);
        int plusPoint = 1;
        // When
        int userPoint = user.getPoint();
        user.setPoint(userPoint + plusPoint);
        userService.updatePoint(user);

        // Then
        assertEquals(userService.getUser(defaultId).getPoint(),
                userPoint+plusPoint);



        // Given
        user = userService.getUser(defaultId);
        int minusPoint = -2;
        // When
        userPoint = user.getPoint();
        user.setPoint(userPoint + minusPoint);
        userService.updatePoint(user);

        // Then
        assertEquals(userService.getUser(defaultId).getPoint(),
                userPoint+minusPoint);

    }
}