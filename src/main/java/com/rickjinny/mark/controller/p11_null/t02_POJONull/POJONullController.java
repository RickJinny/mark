package com.rickjinny.mark.controller.p11_null.t02_POJONull;

import com.rickjinny.mark.controller.p11_null.t02_POJONull.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/pojoNull")
public class POJONullController {

    private UserRepository userRepository;

    @PostMapping(value = "/wrong")
    public User wrong(@RequestBody User user) {
        user.setNickName(String.format("guest%s", user.getName()));
        return userRepository.save(user);
    }
}
