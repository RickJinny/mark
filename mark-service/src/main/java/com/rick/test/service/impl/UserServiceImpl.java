package com.rick.test.service.impl;

import com.rick.test.dao.dao.UserDao;
import com.rick.test.dao.model.User;
import com.rick.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getUserList(List<Integer> userIds) {
        return userDao.getUserList(userIds);
    }
}
