package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.ReviewEventDao;
import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.dto.EventDto;
import com.triple.travelers.event.service.ReviewEventService;
import com.triple.travelers.event.vo.Event;
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
    //리뷰 테이블과 연결된 dao 있다고 가정
    //private final ReviewDao reviewDao;

    @Transactional
    @Override
    public EventDto add(Event request) {
        //validation

        //1. 리뷰테이블에 place_id가 같고 user_id가 다르다는 조건으로
        // 조회해서 리뷰가 있는지 확인
        boolean existed = true;

        //2-1. 리뷰가 없으면 최초 리뷰로 확인 보너스 포인트 +1
        int firstReview = 0;
        int contentPoint = 0;
        int photoPoint = 0;
        if (!existed) firstReview++;
        //2-2. 리뷰가 있으면 최초 리뷰가 아닌 것으로 확인 보너스 포인트 x

        //3. content
        //not null로 설정하였기 때문에 무조건 있다. 포인트 +1
        contentPoint++;

        //4. attachedPhotoIds
        //사진이 한 장 이상이라면 +1점
        //사진이 없다면 null로 표시
        if(!request.getAttachedPhotoIds()
                .isEmpty()) photoPoint++;


        //5. 유저에게 포인트 제공
        User user = userDao.selectUser(request.getUser_id());
        int point = user.getPoint() +
                firstReview + contentPoint + photoPoint;
        user.setPoint(point);
        userDao.updatePoint(user);
        log.info("add point : user_id:{} 최초 리뷰 포인트:{} 내용 포인트:{} 사진 포인트:{} 현재 유저 포인트:{}",
                request.getUser_id(), firstReview, contentPoint, photoPoint, point);

        return getBuildEventDto(request);
    }

    @Transactional
    @Override
    public EventDto mod(Event request) {


        return getBuildEventDto(request);
    }

    @Transactional
    @Override
    public EventDto delete(Event request) {


        return getBuildEventDto(request);
    }

    private EventDto getBuildEventDto(Event request) {
        return EventDto.builder()
                .type(request.getType())
                .action(request.getAction())
                .user_id(request.getUser_id())
                .place_id(request.getPlace_id())
                .review_id(request.getReview_id())
                .build();
    }
}
