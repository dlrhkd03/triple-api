package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.ReviewDao;
import com.triple.travelers.event.dao.ReviewEventDao;
import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.dto.EventDto;
import com.triple.travelers.event.service.ReviewEventService;
import com.triple.travelers.event.vo.Event;
import com.triple.travelers.event.vo.Review;
import com.triple.travelers.event.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewEventServiceLogic implements ReviewEventService {
    private final UserDao userDao;
    private final ReviewEventDao reviewEventDao;
    private final ReviewDao reviewDao;
    //리뷰 테이블과 연결된 dao 있다고 가정
    //private final ReviewDao reviewDao;

    @Transactional
    @Override
    public EventDto add(Event request) {
        //validation

        //리뷰가 작성됨
        reviewDao.insertReview(
                Review.builder()
                        .review_id(request.getReview_id())
                        .place_id(request.getPlace_id())
                        .user_id(request.getUser_id())
                        .build()
        );

        int bonus_point = 0;
        int content_point = 0;
        int photo_point = 0;

        //1. 리뷰테이블에 리뷰 존재 확인
        //place_id가 같고 user_id가 다르다는 조건으로 조회해서 리뷰가 있는지 확인
        boolean notExisted = reviewDao.selectReview(request.getUser_id(), request.getPlace_id()) == null;

        //1-1. 리뷰가 없으면 최초 리뷰로 확인 / 보너스 포인트 +1
        if (notExisted) bonus_point = 1;
        //1-2. 리뷰가 있으면 최초 리뷰가 아닌 것으로 확인 보너스 포인트 x

        //2. content
        //content는 not null 로 설정하였기 때문에 무조건 존재. 포인트 +1
        content_point = 1;

        //3. attachedPhotoIds
        //사진이 한 장 이상이라면 +1점
        //사진이 없다면 null 로 표시된다.
        if (request.getPhoto_url() != null) photo_point = 1;

        //4. 이벤트 생성
        createEvent(request, bonus_point, content_point, photo_point);

        //5. 유저에게 포인트 제공
        int changePoint = bonus_point + content_point + photo_point;
        changeUserPoint(request, changePoint, bonus_point, content_point, photo_point);

        return getBuildEventDto(request);
    }

    @Transactional
    @Override
    public EventDto mod(Event request) {


        //1. 이 유저의 가장 최근 리뷰 이벤트 조회
        Event recentEvent = reviewEventDao.selectRecentEvent(request);

        //2. 보너스 포인트와 content_point 는 그대로
        int bonus_point = recentEvent.getBonus_point();
        int content_point = recentEvent.getContent_point();
        int photo_point = recentEvent.getPhoto_point();

        //3-1. photo_point가 0이라면 (첨부된 사진이 없었다면)
        //3-2. 이번 수정에 사진이 첨부되었다면
        if (photo_point == 0 && request.getPhoto_url() != null) photo_point = 1;

            //4-1. photo_point가 1이라면 (첨부된 사진이 있었다면)
            //4-2 이번 수정에 사진이 삭제되었다면
        else if (photo_point == 1 && request.getPhoto_url() == null) photo_point = 0;

        //5. 이벤트 생성
        createEvent(request, bonus_point, content_point, photo_point);

        //6. 유저에게 포인트 제공
        int changePoint = photo_point - recentEvent.getPhoto_point();
        changeUserPoint(request, changePoint, bonus_point, content_point, photo_point);

        return getBuildEventDto(request);
    }

    @Transactional
    @Override
    public EventDto delete(Event request) {
        //validation

        //리뷰가 삭제됨
        reviewDao.deleteReview(request.getUser_id());

        //1. 이 유저의 가장 최근 리뷰 이벤트 조회
        Event recentEvent = reviewEventDao.selectRecentEvent(request);

        //2. 이벤트 생성
        createEvent(request, 0, 0, 0);

        //3. 유저에게 포인트 회수
        int changePoint = -(recentEvent.getBonus_point() + recentEvent.getPhoto_point() + recentEvent.getPhoto_point());
        changeUserPoint(request, changePoint, 0, 0, 0);

        return getBuildEventDto(request);
    }

    private void createEvent(Event request, int bonus_point, int content_point, int photo_point) {
        request.setBonus_point(bonus_point);
        request.setContent_point(content_point);
        request.setPhoto_point(photo_point);
        reviewEventDao.insertEvent(request);
    }

    private void changeUserPoint(Event request, int changePoint, int bonus_point, int content_point, int photo_point) {
        User user = userDao.selectUser(request.getUser_id());
        int beforeUserPoint = user.getPoint();
        int afterUserPoint = beforeUserPoint + changePoint;
        user.setPoint(afterUserPoint);
        userDao.updatePoint(user);

        //포인트 증감 발생 로그
        log.info("add point : user_id:{} 최초 리뷰 포인트:{} 내용 포인트:{} 사진 포인트:{} 유저 기존 포인트:{} 유저 현재 포인트:{}",
                request.getUser_id(), bonus_point, content_point, photo_point, beforeUserPoint, afterUserPoint);
    }

    private EventDto getBuildEventDto(Event request) {
        return EventDto.builder()
                .type(request.getType())
                .action(request.getAction())
                .user_id(request.getUser_id())
                .place_id(request.getPlace_id())
                .review_id(request.getReview_id())
                .bonus_point(request.getBonus_point())
                .content_point(request.getContent_point())
                .photo_point(request.getPhoto_point())
                .build();
    }
}
