package com.rick.test.service.impl;

import com.google.common.collect.Lists;
import com.rick.common.ServerResponse;
import com.rick.service.CacheService;
import com.rick.test.dao.dao.UserInfoDao;
import com.rick.test.dao.model.UserInfo;
import com.rick.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public ServerResponse<List<UserInfoVO>> getUserInfos() {
        List<UserInfoVO> userInfoList = Lists.newArrayList();
        try {
            List<UserInfo> userInfos = userInfoDao.getUserInfos();
            if (CollectionUtils.isNotEmpty(userInfos)) {
                userInfoList = userInfos.stream().map(userInfo -> {
                    UserInfoVO userInfoVO = new UserInfoVO();
                    userInfoVO.setId(userInfo.getId());
                    userInfoVO.setName(userInfo.getName());
                    userInfoVO.setAge(userInfo.getAge());
                    return userInfoVO;
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("CacheServiceImpl getUserInfos errorMsg: {}", e.getMessage(), e);
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(userInfoList);
    }
}
