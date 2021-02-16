package com.rick.test.dao.dao;

import com.google.common.collect.Lists;
import com.rick.test.dao.mapper.UserMapper;
import com.rick.test.dao.model.User;
import com.rick.test.dao.model.UserExample;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDao {

    @Autowired
    private UserMapper userMapper;

    public List<User> getUserList(List<Integer> userIds) {
        UserExample example = new UserExample();
        example.createCriteria().andUserIdIn(userIds);
        List<User> userList = userMapper.selectByExample(example);
        return CollectionUtils.isEmpty(userList) ? Lists.newArrayList() : userList;
    }
}
