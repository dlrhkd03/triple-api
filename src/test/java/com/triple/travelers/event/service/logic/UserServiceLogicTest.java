package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.exception.EventErrorCode;
import com.triple.travelers.event.exception.EventException;
import com.triple.travelers.event.vo.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceLogicTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserServiceLogic userService;

    private final String defaultId = "a62bac06-86ba-4ab6-b467-a01b60e6b66a";

    @AfterEach
    void after() {
        if(userDao.selectUser(defaultId) != null) userService.deleteUser(defaultId);
    }

    @DisplayName("디폴트 유저 생성 후 조회")
    @Test
    void createUserTest() {
        //when
        userService.addUser(defaultId);
        User user = userService.getUser(defaultId);
        //then
        assertEquals(user.getUser_id(), defaultId);
        assertEquals(user.getPoint(), 0);
    }

    @DisplayName("디폴트 유저 생성 후 동일한 유저 생성")
    @Test
    void createUserTest_failed_with_duplicated_user() {
        //when
        userService.addUser(defaultId);
        EventException eventException = assertThrows(EventException.class,
                () -> userService.addUser(defaultId));
        //then
        assertEquals(EventErrorCode.DUPLICATED_USER, eventException.getEventErrorCode());
    }

    @DisplayName("존재하지 않는 유저 포인트 조회")
    @Test
    void retrievePointTest_failed_with_not_existed_user() {
        //when
        EventException eventException = assertThrows(EventException.class,
                () -> userService.getPoint(defaultId));
        //then
        assertEquals(EventErrorCode.NO_USER, eventException.getEventErrorCode());
    }

    @DisplayName("유저 포인트 변경")
    @Test
    void updatePointTest() {
        // Given
        userService.addUser(defaultId);
        User user = userService.getUser(defaultId);
        int plusPoint = 1;
        // When
        //포인트 0 -> 1
        int beforeUserPoint1 = user.getPoint();
        user.setPoint(beforeUserPoint1 + plusPoint);
        userService.changePoint(user);
        int afterUserPoint1 = userService.getUser(defaultId).getPoint();

        //포인트 1 -> 2
        int beforeUserPoint2 = user.getPoint();
        user.setPoint(beforeUserPoint2 + plusPoint);
        userService.changePoint(user);
        int afterUserPoint2 = userService.getUser(defaultId).getPoint();

        // Then
        assertEquals(afterUserPoint1, 1);
        assertEquals(afterUserPoint2, 2);


        // Given
        user = userService.getUser(defaultId);
        int minusPoint = -1;
        // When
        //포인트 2 -> 1
        int beforeUserPoint3 = user.getPoint();
        user.setPoint(beforeUserPoint3 + minusPoint);
        userService.changePoint(user);
        int afterUserPoint3 = userService.getUser(defaultId).getPoint();

        //포인트 1 -> 0
        int beforeUserPoint4 = user.getPoint();
        user.setPoint(beforeUserPoint4 + minusPoint);
        userService.changePoint(user);
        int afterUserPoint4 = userService.getUser(defaultId).getPoint();

        // Then
        assertEquals(afterUserPoint3, 1);
        assertEquals(afterUserPoint4, 0);
    }
}