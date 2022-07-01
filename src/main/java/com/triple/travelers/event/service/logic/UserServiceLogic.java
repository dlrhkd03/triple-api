package com.triple.travelers.event.service.logic;

import com.triple.travelers.event.dao.UserDao;
import com.triple.travelers.event.exception.EventErrorCode;
import com.triple.travelers.event.exception.EventException;
import com.triple.travelers.event.service.UserService;
import com.triple.travelers.event.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceLogic implements UserService {
    private final UserDao userDao;

    @Override
    @Transactional
    public void addUser(String user_id) {
        User user = userDao.selectUser(user_id);
        creationValidation(user);

        log.info("create user : user_id :{}", user_id);
        userDao.createUser(user_id);
    }

    @Override
    public User getUser(String user_id) {
        User user = userDao.selectUser(user_id);
        noUserValidation(user);

        return user;
    }

    @Override
    public int getPoint(String user_id) {
        User user = userDao.selectUser(user_id);
        noUserValidation(user);

        log.info("lookup point : user_id :{} user total point :{}", user_id, user.getPoint());
        return user.getPoint();
    }

    @Override
    @Transactional
    public void changePoint(User user) {
        log.info("change point : user_id{} user point :{}", user.getUser_id(), user.getPoint());
        userDao.updatePoint(user);
    }

    @Override
    @Transactional
    public void deleteUser(String user_id) {
        noUserValidation(userDao.selectUser(user_id));

        userDao.deleteUser(user_id);
    }

    private void creationValidation(User user) {
        if (user != null) {
            throw new EventException(EventErrorCode.DUPLICATED_USER);
        }
    }

    private void noUserValidation(User user) {
        if (user == null) {
            throw new EventException(EventErrorCode.NO_USER);
        }
    }

}
