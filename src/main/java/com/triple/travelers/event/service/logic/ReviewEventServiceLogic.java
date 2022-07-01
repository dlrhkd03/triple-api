package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.ReviewDao;
import com.triple.travelers.event.dao.ReviewEventDao;
import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.dto.EventDto;
import com.triple.travelers.event.exception.EventException;
import com.triple.travelers.event.exception.ReviewException;
import com.triple.travelers.event.service.ReviewEventService;
import com.triple.travelers.event.vo.Event;
import com.triple.travelers.event.vo.Review;
import com.triple.travelers.event.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.triple.travelers.event.exception.EventErrorCode.DUPLICATED_EVENT;
import static com.triple.travelers.event.exception.EventErrorCode.NO_USER;
import static com.triple.travelers.event.exception.ReviewErrorCode.NO_REVIEW;
import static com.triple.travelers.event.exception.ReviewErrorCode.USER_ID_REVIEW_ID_NOT_MATCHED;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewEventServiceLogic implements ReviewEventService {
    private final UserDao userDao;
    private final ReviewEventDao reviewEventDao;
    private final ReviewDao reviewDao;

    @Transactional
    @Override
    public Event add(EventDto request) {
        //http 테스트에서 사용할 가상 리뷰 생성
        if (reviewDao.selectReviewById("240a0658-dc5f-4878-9381-ebb7b2667772") == null) {
            reviewDao.insertReview(Review.builder()
                    .place_id(request.getPlaceId())
                    .review_id("240a0658-dc5f-4878-9381-ebb7b2667772")
                    .user_id(request.getUserId())
                    .build());
        }

        //validation
        addValidation(request);

        //유저 테이블에 유저가 없다면 생성
        if (userDao.selectUser(request.getUserId()) == null) userDao.createUser(request.getUserId());

        int bonus_point = 0;
        int content_point = 0;
        int photo_point = 0;

        //1. 리뷰테이블에 리뷰 존재 확인
        //place_id가 같고 user_id가 다르다는 조건으로 조회해서 리뷰가 있는지 확인
        boolean notExisted = reviewDao.selectReviewByUserAndPlace(request.getUserId(), request.getPlaceId()) == null;

        //1-1. 리뷰가 없으면 최초 리뷰로 확인 / 보너스 포인트 +1
        if (notExisted) bonus_point = 1;
        //1-2. 리뷰가 있으면 최초 리뷰가 아닌 것으로 확인 보너스 포인트 x

        //2. content
        //content는 not null 로 설정하였기 때문에 무조건 존재. 포인트 +1
        content_point = 1;

        //3. attachedPhotoIds
        //사진이 한 장 이상이라면 +1점
        //사진이 없다면 길이는 0
        if (request.getAttachedPhotoIds().length != 0) photo_point = 1;

        //4. 이벤트 생성
        createEvent(request, bonus_point, content_point, photo_point);

        //5. 유저에게 포인트 제공
        int changePoint = bonus_point + content_point + photo_point;
        changeUserPoint(request, changePoint, bonus_point, content_point, photo_point);

        return getBuildEvent(request, bonus_point, content_point, photo_point);
    }

    @Transactional
    @Override
    public Event mod(EventDto request) {
        //validation
        validation(request);
        userValidation(request.getUserId());


        //1. 이 유저의 가장 최근 리뷰 이벤트 조회
        Event recentEvent = reviewEventDao.selectRecentEvent(request);

        //2. 보너스 포인트와 content_point 는 그대로
        int bonus_point = recentEvent.getBonus_point();
        int content_point = recentEvent.getContent_point();
        int photo_point = recentEvent.getPhoto_point();

        //3-1. photo_point가 0이라면 (첨부된 사진이 없었다면)
        //3-2. 이번 수정에 사진이 첨부되었다면
        if (photo_point == 0 && request.getAttachedPhotoIds().length != 0) photo_point = 1;

        //4-1. photo_point가 1이라면 (첨부된 사진이 있었다면)
        //4-2 이번 수정에 사진이 삭제되었다면
        else if (photo_point == 1 && request.getAttachedPhotoIds().length == 0) photo_point = 0;

        //5. 이벤트 생성
        createEvent(request, bonus_point, content_point, photo_point);

        //6. 유저에게 포인트 제공
        int changePoint = photo_point - recentEvent.getPhoto_point();
        changeUserPoint(request, changePoint, bonus_point, content_point, photo_point);

        return getBuildEvent(request, bonus_point, content_point, photo_point);
    }

    @Transactional
    @Override
    public Event delete(EventDto request) {
        //validation
        deleteValidation(request);

        //1. 이 유저의 가장 최근 리뷰 이벤트 조회
        Event recentEvent = reviewEventDao.selectRecentEvent(request);

        //2. 이벤트 생성
        createEvent(request, 0, 0, 0);

        //3. 유저에게 포인트 회수
        int changePoint = -(recentEvent.getBonus_point() + recentEvent.getContent_point() + recentEvent.getPhoto_point());
        changeUserPoint(request, changePoint, 0, 0, 0);

        return getBuildEvent(request, 0, 0, 0);
    }

    private void validation(EventDto request) {

        Review review = reviewDao.selectReviewById(request.getReviewId());
        if (review == null) {
            throw new ReviewException(NO_REVIEW);
        }

        if (!review.getUser_id().equals(request.getUserId())) {
            throw new ReviewException(USER_ID_REVIEW_ID_NOT_MATCHED);
        }

    }

    private void userValidation(String user_id) {
        if (userDao.selectUser(user_id) == null) {
            throw new EventException(NO_USER);
        }
    }

    private void createEvent(EventDto request, int bonus_point, int content_point, int photo_point) {
        Event event = getBuildEvent(request, bonus_point, content_point, photo_point);

        reviewEventDao.insertEvent(event);
    }

    private Event getBuildEvent(EventDto request, int bonus_point, int content_point, int photo_point) {
        StringBuilder sb = new StringBuilder();
        if (request.getAttachedPhotoIds().length != 0) {

            for (String str : request.getAttachedPhotoIds()) {
                sb.append(str).append(",");
            }
        }
        return Event.builder()
                .type(request.getType())
                .action(request.getAction())
                .review_id(request.getReviewId())
                .content(request.getContent())
                .photo_ids(sb.toString())
                .user_id(request.getUserId())
                .place_id(request.getPlaceId())
                .bonus_point(bonus_point)
                .content_point(content_point)
                .photo_point(photo_point)
                .build();
    }

    private void changeUserPoint(EventDto request, int changePoint, int bonus_point, int content_point, int photo_point) {
        User user = userDao.selectUser(request.getUserId());
        int beforeUserPoint = user.getPoint();
        int afterUserPoint = beforeUserPoint + changePoint;
        user.setPoint(afterUserPoint);
        userDao.updatePoint(user);

        //포인트 증감 발생 로그
        log.info("change point: type:{}, action:{} user_id:{} 최초 리뷰 포인트:{} 내용 포인트:{} 사진 포인트:{} 유저 기존 포인트:{} 유저 현재 포인트:{}",
                request.getType(), request.getAction(), request.getUserId(), bonus_point, content_point, photo_point, beforeUserPoint, afterUserPoint);
    }

    private void addValidation(EventDto request) {
        validation(request);
        //리뷰도 있고, 이벤트 add 인게 있다.
        if (reviewEventDao.selectEventAdd(request.getUserId(), request.getReviewId()) != null) {
            throw new EventException(DUPLICATED_EVENT);
        }
    }

    private void deleteValidation(EventDto request) {
        userValidation(request.getUserId());
        if (reviewEventDao.selectRecentEvent(request).getAction().equals("DELETE")) {
            throw new ReviewException(NO_REVIEW);
        }
    }
}
