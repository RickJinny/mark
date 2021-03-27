package com.rick.service;

import com.rick.vo.UserVO;

import java.util.List;

public interface UserService {

    List<UserVO> getUserList(List<Long> userIds);

}
