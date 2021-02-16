package com.rick.test.util.powermock.service;

import com.rick.test.util.powermock.dao.UserDao;
import com.rick.test.util.powermock.model.User;

public class UserService01 {

    private UserDao userDao;

    public UserService01(UserDao userDao) {
        this.userDao = userDao;
    }

    public int queryUserCount() {
        return userDao.getCount();
    }

    public void saveUser(User user) {
        userDao.insertUser(user);
    }
}
