package com.rick.test.service;

import com.rick.test.dao.model.User;

import java.util.List;

public interface UserService {

    List<User> getUserList(List<Integer> userIds);

}
