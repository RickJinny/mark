package com.rick.test.controller;

import com.rick.common.ServerResponse;
import com.rick.service.CacheService;
import com.rick.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/getUserInfos")
    public ServerResponse<List<UserInfoVO>> getUserInfos(Long id) {
        return cacheService.getUserInfos(id);
    }

    @RequestMapping(value = "/getUserInfos/all")
    public ServerResponse<List<UserInfoVO>> getAllUserInfos() {
        return cacheService.getAllUserInfos();
    }
}
