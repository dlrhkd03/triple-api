package com.triple.travelers.event.dao;

import com.triple.travelers.event.vo.Review;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ReviewDao {
    void insertReview(Review review);

    Review selectReviewByUserAndPlace(String user_id, String place_id);

    Review selectReviewById(String review_id);

    void recoverReview(Review review);

    void deleteReview(String review_id);

    Review selectAllStatusReviewById(String review_id);
}
