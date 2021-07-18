package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.vo.UserInfoVO;

import java.util.List;

public interface CacheService {

    ServerResponse<List<UserInfoVO>> getUserInfos(Long id);

    ServerResponse<List<UserInfoVO>> getAllUserInfos();

}
