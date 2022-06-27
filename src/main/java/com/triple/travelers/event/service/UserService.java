package com.triple.travelers.event.service;

import com.triple.travelers.event.vo.User;

public interface UserService {
    void createUser(User user);
    User getUser(String user_id);
    void updatePoint(User user);
    void deleteUser(String user_id);
}
