package com.rick.test.service.impl;

import com.rick.service.UserService;
import com.rick.test.dao.dao.UserDao;
import com.rick.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<UserVO> getUserList(List<Long> userIds) {
        return userDao.getUserList(userIds);
    }
}
