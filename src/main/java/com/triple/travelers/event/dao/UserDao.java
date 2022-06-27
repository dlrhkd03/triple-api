package com.triple.travelers.event.dao;

import com.triple.travelers.event.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    User selectUser(String user_id);

    void updatePoint(User user);

    void createUser(User user);

    void deleteUser(String user_id);
}
