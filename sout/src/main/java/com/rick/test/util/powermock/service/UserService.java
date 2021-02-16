package com.rick.test.util.powermock.service;

import com.rick.test.util.powermock.dao.UserDao;
import com.rick.test.util.powermock.model.User;

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
