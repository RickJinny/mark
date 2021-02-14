package com.rick.test.powermock.service;

import com.rick.test.powermock.dao.UserDao;
import com.rick.test.powermock.model.User;

public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public int queryUserCount() {
        return userDao.getCount();
    }

    public void saveUser(User user) {
        userDao.insertUser(user);
    }
}
