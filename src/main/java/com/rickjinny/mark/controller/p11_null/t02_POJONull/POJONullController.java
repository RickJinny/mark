package com.rickjinny.mark.controller.p11_null.t02_POJONull;

import com.rickjinny.mark.controller.p11_null.t02_POJONull.bean.User;
import com.rickjinny.mark.controller.p11_null.t02_POJONull.bean.UserDto;
import com.rickjinny.mark.controller.p11_null.t02_POJONull.bean.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/pojoNull")
public class POJONullController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @PostMapping(value = "/wrong")
    public User wrong(@RequestBody User user) {
        user.setNickName(String.format("guest%s", user.getName()));
        return userRepository.save(user);
    }

    @PostMapping(value = "/right")
    public UserEntity right(@RequestBody UserDto userDto) {
        if (userDto == null || userDto.getId() == null) {
            throw new IllegalArgumentException("用户id不能为空行");
        }

        UserEntity userEntity = userEntityRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        if (userDto.getName() != null) {
            userEntity.setName(userDto.getName().orElse(""));
        }
        userEntity.setNickName("guest" + userEntity.getName());
        if (userDto.getAge() != null) {
            userEntity.setAge(userDto.getAge().orElseThrow(() -> new IllegalArgumentException("年龄不能为空")));
        }
        return userEntityRepository.save(userEntity);
    }
}
