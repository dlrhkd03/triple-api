package com.triple.travelers.event.dao;

import com.triple.travelers.event.vo.Event;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReviewEventDao {
    List<Event> selectEventListByPlace(String place_id);

    Event selectEventByReviewAndUserAndPlace(String review_id, String user_id, String place_id);

    void insertEvent(Event request);

    void updateEvent(Event request);

    void deleteEvent(String review_id, String user_id, String place_id);
}
