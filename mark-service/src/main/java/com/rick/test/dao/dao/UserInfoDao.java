package com.rick.test.dao.dao;

import com.rick.test.dao.mapper.UserInfoMapper;
import com.rick.test.dao.model.UserInfo;
import com.rick.test.dao.model.UserInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfoDao {

    @Autowired
    private UserInfoMapper userInfoMapper;

    public List<UserInfo> getUserInfos() {
        UserInfoExample userInfoExample = new UserInfoExample();
        return userInfoMapper.selectByExample(userInfoExample);
    }

}
