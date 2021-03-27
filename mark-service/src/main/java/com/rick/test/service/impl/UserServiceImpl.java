package com.rick.test.service.impl;

import com.rick.common.ServerResponse;
import com.rick.service.UserService;
import com.rick.test.dao.dao.UserDao;
import com.rick.test.dao.model.User;
import com.rick.vo.CreateUserRequest;
import com.rick.vo.CreateUserResponse;
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

    @Override
    public ServerResponse<CreateUserResponse> createUser(CreateUserRequest request) {
        User user = userDao.addUser(request);
        if (user == null || user.getUserId() == null) {
            return ServerResponse.createByErrorMessage("创建用户失败");
        }
        CreateUserResponse response = CreateUserResponse.builder().userId(user.getUserId()).build();
        return ServerResponse.createBySuccess(response);
    }
}
