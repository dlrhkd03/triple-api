package com.triple.travelers.event.dao;

import com.triple.travelers.event.vo.Review;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReviewDao {
    void insertReview(Review review);

    Review selectReviewByUserAndPlace(String user_id, String place_id);

    Review selectReviewById(String review_id);

    void deleteReview(String review_id);
}
