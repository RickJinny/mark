package com.rick.test.dao.dao;

import com.rick.test.dao.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoDao {

    @Autowired
    private UserInfoMapper userInfoMapper;



}
