package com.triple.travelers.event.dao;

import com.triple.travelers.event.vo.Review;

public interface ReviewDao {
    void insertReview(Review review);

    Review selectReview(String user_id, String place_id);

    void deleteReview(String user_id);
}
