package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.vo.UserInfoVO;

import java.util.List;

public interface CacheService {

    ServerResponse<List<UserInfoVO>> getUserInfos();

}
