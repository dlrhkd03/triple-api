package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.ReviewDao;
import com.triple.travelers.event.dao.ReviewEventDao;
import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.dto.EventDto;
import com.triple.travelers.event.exception.ReviewException;
import com.triple.travelers.event.vo.Event;
import com.triple.travelers.event.vo.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.triple.travelers.event.exception.ReviewErrorCode.NO_REVIEW;
import static com.triple.travelers.event.exception.ReviewErrorCode.USER_ID_REVIEW_ID_NOT_MATCHED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReviewEventServiceLogicTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ReviewEventDao reviewEventDao;

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private ReviewEventServiceLogic reviewEventServiceLogic;

    @BeforeEach
    void before() {

    }

    private final Review review1 = Review.builder()
            .review_id("test reviewId1")
            .place_id("test placeId")
            .user_id("test user1")
            .build();

    private final Review review2 = Review.builder()
            .review_id("test reviewId2")
            .place_id("test placeId")
            .user_id("test user2")
            .build();

    //리뷰가 작성된걸 확인하기 위한 생성
    //실제론 이 api 에서 생성되는 것이 아님
    //리뷰1 생성메서드
    public void createReview1() {
        if (reviewDao.selectAllStatusReviewById(review1.getReview_id()) != null) {
            reviewDao.recoverReview(review1);
        } else {
            reviewDao.insertReview(review1);
        }
    }

    //리뷰2 생성메서드
    public void createReview2() {
        if (reviewDao.selectAllStatusReviewById(review2.getReview_id()) != null) {
            reviewDao.recoverReview(review2);
        } else {
            reviewDao.insertReview(review2);
        }
    }

    @AfterEach
    void after() {
        userDao.deleteUser("test user1");
        userDao.deleteUser("test user2");
        reviewDao.deleteReview("test reviewId1");
        reviewDao.deleteReview("test reviewId2");
        if (reviewEventDao.selectEvent("test user1", "test reviewId1") != null) {
            reviewEventDao.deleteEvent("test user1", "test reviewId1");
        }
        if (reviewEventDao.selectEvent("test user2", "test reviewId2") != null) {
            reviewEventDao.deleteEvent("test user2", "test reviewId2");
        }
    }

    //photo 2개 넣은 user1
    private final EventDto requestADD1 = EventDto.builder()
            .type("REVIEW")
            .action("ADD")
            .reviewId("test reviewId1")
            .content("test")
            .attachedPhotoIds(new String[]{"test photo1", "test photo2"})
            .userId("test user1")
            .placeId("test placeId")
            .build();

    //photo 없는 user2
    private final EventDto requestADD2 = EventDto.builder()
            .type("REVIEW")
            .action("ADD")
            .reviewId("test reviewId2")
            .content("test")
            .attachedPhotoIds(new String[]{})
            .userId("test user2")
            .placeId("test placeId")
            .build();

    //photo 지운 user1
    private final EventDto requestMOD1 = EventDto.builder()
            .type("REVIEW")
            .action("MOD")
            .reviewId("test reviewId1")
            .content("test")
            .attachedPhotoIds(new String[]{})
            .userId("test user1")
            .placeId("test placeId")
            .build();

    //photo 넣은 user2
    private final EventDto requestMOD2 = EventDto.builder()
            .type("REVIEW")
            .action("ADD")
            .reviewId("test reviewId2")
            .content("test")
            .attachedPhotoIds(new String[]{"test photo1"})
            .userId("test user2")
            .placeId("test placeId")
            .build();

    //유저2 삭제
    private final EventDto requestDEL2 = EventDto.builder()
            .type("REVIEW")
            .action("DELETE")
            .reviewId("test reviewId2")
            .content("")
            .attachedPhotoIds(new String[]{})
            .userId("test user2")
            .placeId("test placeId")
            .build();

    @Test
    @DisplayName("없는 유저를 이벤트로 받으면 validation 되는지 확인")
    void validationTest1() {
        //given
        createReview1();

        //when
        ReviewException reviewException = assertThrows(ReviewException.class,
                () -> reviewEventServiceLogic.add(EventDto.builder()
                        .type("REVIEW")
                        .action("ADD")
                        .reviewId("test reviewId1")
                        .content("test")
                        .userId("test user")
                        .placeId("test placeId")
                        .build()));
        //then

        assertEquals(USER_ID_REVIEW_ID_NOT_MATCHED, reviewException.getReviewErrorCode());
    }

    @Test
    @DisplayName("없는 리뷰를 이벤트로 받으면 validation 되는지 확인")
    void validationTest2() {
        //given
        createReview1();

        //when
        ReviewException reviewException = assertThrows(ReviewException.class,
                () -> reviewEventServiceLogic.add(EventDto.builder()
                        .type("REVIEW")
                        .action("ADD")
                        .reviewId("test reviewId")
                        .content("test")
                        .userId("test user1")
                        .placeId("test placeId")
                        .build()));

        //then
        assertEquals(NO_REVIEW, reviewException.getReviewErrorCode());
    }

    @Test
    @DisplayName("유저도 있고 리뷰도 있지만 서로 맞지 않을 때 validation 되는지 확인")
    void validationTest3() {
        //given
        createReview1();

        //when
        ReviewException notMatchedException = assertThrows(ReviewException.class,
                () -> reviewEventServiceLogic.add(EventDto.builder()
                        .type("REVIEW")
                        .action("ADD")
                        .reviewId("test reviewId1")
                        .content("test")
                        .userId("test user2")
                        .placeId("test placeId")
                        .build()));

        //then
        assertEquals(USER_ID_REVIEW_ID_NOT_MATCHED, notMatchedException.getReviewErrorCode());
    }

    @Test
    @DisplayName("최초리뷰시 보너스 점수 받는지 확인")
    void bonusPointTest1() {
        // Given
        createReview1();
        // When
        Event event = reviewEventServiceLogic.add(requestADD1);
        // Then
        assertEquals(event.getBonus_point(), 1);
        assertEquals(event.getContent_point(), 1);
        assertEquals(event.getPhoto_point(), 1);
    }

    @Test
    @DisplayName("최초리뷰가 아닐시 보너스 점수 받지 않는지 확인")
    void bonusPointTest2() throws InterruptedException {
        // Given
        createReview1();
        reviewEventServiceLogic.add(requestADD1);
        createReview2();
        // When

        Thread.sleep(1000);
        Event event = reviewEventServiceLogic.add(requestADD2);
        // Then
        // 리뷰2의 보너스포인트는 0이다.
        assertEquals(event.getBonus_point(), 0);
    }

    @Test
    @DisplayName("유저2가 리뷰를 추가하고 삭제한 후에 유저1이 리뷰 작성하면 최초리뷰 보너스를 받는지 확인")
    void bonusPointTest3() throws InterruptedException {
        // Given
        // When
        createReview2();
        reviewEventServiceLogic.add(requestADD2);

        Thread.sleep(1000);
        reviewDao.deleteReview(requestADD2.getReviewId());
        reviewEventServiceLogic.delete(requestDEL2);

        createReview1();
        Event event = reviewEventServiceLogic.add(requestADD1);
        // Then
        assertEquals(event.getBonus_point(), 1);
    }

    @Test
    @DisplayName("유저2가 리뷰를 추가하고 유저1이 리뷰 추가한 후에 유저2가 리뷰 삭제하면 최초리뷰 보너스 유저1이 받지 않는지 확인")
    void bonusPointTest4() throws InterruptedException {
        // Given
        // When
        createReview2();
        reviewEventServiceLogic.add(requestADD2);

        Thread.sleep(1000);
        createReview1();
        reviewEventServiceLogic.add(requestADD1);
        int user1_before_point = userDao.selectUser(requestADD1.getUserId()).getPoint();

        Thread.sleep(1000);
        reviewDao.deleteReview(requestADD2.getReviewId());
        reviewEventServiceLogic.delete(requestDEL2);

        int user1_after_point = userDao.selectUser(requestADD1.getUserId()).getPoint();
        // Then
        //유저1의 총 포인트 변화 X
        assertEquals(user1_after_point - user1_before_point, 0);
    }

    @Test
    @DisplayName("유저1이 포토 보너스 받은 후에 포토 지우면 포토 점수 회수하는지 확인")
    void retrieveTest1() throws InterruptedException {
        // Given
        // When
        String user_id = requestADD1.getUserId();

        createReview1();
        reviewEventServiceLogic.add(requestADD1);
        Event event1 = reviewEventDao.selectRecentEvent(requestADD1);
        int user1_before_point = userDao.selectUser(user_id).getPoint();

        Thread.sleep(1000);
        reviewEventServiceLogic.mod(requestMOD1);
        Event event2 = reviewEventDao.selectRecentEvent(requestADD1);
        int user1_after_point = userDao.selectUser(user_id).getPoint();
        // Then
        assertEquals(event1.getPhoto_point(), 1);
        assertEquals(event2.getPhoto_point(), 0);
        assertEquals(user1_before_point - user1_after_point, 1);
    }

    @Test
    @DisplayName("유저2가 포토없다가 포토 추가하면 포토 점수 받는지 확인")
    void retrieveTest2() throws InterruptedException {
        // Given
        // When
        String user_id = requestADD2.getUserId();

        createReview2();
        reviewEventServiceLogic.add(requestADD2);
        Event event1 = reviewEventDao.selectRecentEvent(requestADD2);
        int user2_before_point = userDao.selectUser(user_id).getPoint();

        Thread.sleep(1000);
        reviewEventServiceLogic.mod(requestMOD2);
        Event event2 = reviewEventDao.selectRecentEvent(requestADD2);
        int user2_after_point = userDao.selectUser(user_id).getPoint();
        // Then
        assertEquals(event1.getPhoto_point(), 0);
        assertEquals(event2.getPhoto_point(), 1);
        assertEquals(user2_after_point - user2_before_point, 1);
    }
}