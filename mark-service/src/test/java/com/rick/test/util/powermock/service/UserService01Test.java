package com.rick.test.util.powermock.service;

import com.rick.test.util.powermock.dao.UserDao;
import com.rick.test.util.powermock.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserService01Test {

    private UserService01 userService01;

    public void setUp() {
        userService01 = new UserService01(new UserDao());
    }

    @Test
    public void testQueryUserCount() {
        int count = userService01.queryUserCount();
        assertEquals(0, count);
    }

    @Test
    public void testSaveUser(User user) {

    }
}
