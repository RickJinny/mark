package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.request.CreateUserRequest;
import com.rick.response.CreateUserResponse;
import com.rick.vo.UserVO;

import java.util.List;

public interface UserService {

    List<UserVO> getUserList(List<Long> userIds);

    ServerResponse<CreateUserResponse> createUser(CreateUserRequest request);

}
