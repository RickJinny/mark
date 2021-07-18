package com.rick.test.service.impl;

import com.rick.common.ServerResponse;
import com.rick.service.CacheService;
import com.rick.vo.UserInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheServiceImpl implements CacheService {

    @Override
    public ServerResponse<List<UserInfoVO>> getUserInfos() {
        return null;
    }
}
