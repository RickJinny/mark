package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.vo.CreateUserRequest;
import com.rick.vo.CreateUserResponse;
import com.rick.vo.UserVO;

import java.util.List;

public interface UserService {

    List<UserVO> getUserList(List<Long> userIds);

    ServerResponse<CreateUserResponse> createUser(CreateUserRequest request);

}
