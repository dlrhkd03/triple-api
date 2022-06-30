package com.triple.travelers.event.dao;

import com.triple.travelers.event.vo.Event;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReviewEventDao {
    List<Event> selectEvent(String user_id, String review_id);

    void insertEvent(Event event);

    Event selectRecentEvent(Event event);

    void deleteEvent(String user_id, String review_id);
}
