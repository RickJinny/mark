package com.rick.test.dao.dao;

import com.google.common.collect.Lists;
import com.rick.test.dao.mapper.UserMapper;
import com.rick.test.dao.model.User;
import com.rick.test.dao.model.UserExample;
import com.rick.vo.CreateUserRequest;
import com.rick.vo.UserVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDao {

    @Autowired
    private UserMapper userMapper;

    public List<UserVO> getUserList(List<Long> userIds) {
        UserExample example = new UserExample();
        example.createCriteria().andUserIdIn(userIds);
        List<User> userList = userMapper.selectByExample(example);
        return CollectionUtils.isEmpty(userList) ? Lists.newArrayList() : assembleUserVOS(userList);
    }

    private List<UserVO> assembleUserVOS(List<User> userList) {
        return userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            userVO.setUserId(user.getUserId());
            userVO.setUserName(user.getUserName());
            return userVO;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public User addUser(CreateUserRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        user.setUserType(request.getUserType());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int rowCount = userMapper.insertSelective(user);
        if (rowCount > 0) {
            return user;
        }
        return null;
    }
}
