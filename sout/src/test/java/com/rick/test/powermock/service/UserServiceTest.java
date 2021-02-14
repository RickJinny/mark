package com.rick.test.powermock.service;

import com.rick.test.powermock.dao.UserDao;
import com.rick.test.powermock.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {

    private UserService userService;

    public void setUp() {
        userService = new UserService(new UserDao());
    }

    @Test
    public void testQueryUserCount() {
        int count = userService.queryUserCount();
        assertEquals(0, count);
    }

    @Test
    public void testSaveUser(User user) {

    }
}
