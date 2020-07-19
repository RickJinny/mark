package com.rickjinny.mark.controller.p06_transaction.t01_TransactionProxyFailed;

import com.rickjinny.mark.controller.p06_transaction.bean.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transactionProxyFailed")
@Slf4j
public class TransactionProxyFailedController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/wrong1")
    public int wrong1(@RequestParam("name") String name) {
        return userService.createUserWrong1(name);
    }

    @RequestMapping(value = "/wrong2")
    public int wrong2(@RequestParam("name") String name) {
        return userService.createWrong2(name);
    }

    /**
     * 虽然在 UserService 内部注入自己调用的 createUserPublic 可以正确实现事务。
     * 但是使用这个方法调用，比较奇怪，注入的时候自己调用自己，比较奇怪。
     * 使用下面的方式，在 Controller 里面直接调用。
     */
    @RequestMapping(value = "/right1")
    public int right1(@RequestParam("name") String name) {
        return userService.createUserRight(name);
    }

    /**
     * 在 Controller 层直接调用 UserService 的 createUserPublic() 方法即可。
     */
    @RequestMapping(value = "/right2")
    public int right2(@RequestParam("name") String name) {
        try {
            userService.createUserPublic(new UserEntity(name));
        } catch (Exception e) {
            log.error("create user failed because {}", e.getMessage());
        }
        return userService.getUserCount(name);
    }
}
