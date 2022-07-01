package com.triple.travelers.event.service;

import com.triple.travelers.event.vo.User;

public interface UserService {
    void addUser(String user_id);
    User getUser(String user_id);
    int getPoint(String user_id);
    void changePoint(User user);
    void deleteUser(String user_id);
}
