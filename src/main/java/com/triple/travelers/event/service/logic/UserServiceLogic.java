package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.service.UserService;
import com.triple.travelers.event.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceLogic implements UserService {
    private final UserDao userDao;

    @Override
    public void createUser(User user) {
        userDao.createUser(user);
    }

    @Override
    public User getUser(String user_id) {
        //validation
        User user = userDao.selectUser(user_id);

        return user;
    }

    @Override
    public void updatePoint(User user) {
        userDao.updatePoint(user);
    }

    @Override
    public void deleteUser(String user_id) {
        userDao.deleteUser(user_id);
    }

}
