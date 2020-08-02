package com.rickjinny.mark.controller.p27_clientdata.t04_TrustClientUserid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping(value = "/trustClientUserId")
public class TrustClientUserIdController {

    @RequestMapping(value = "/wrong")
    public String wrong(@RequestParam("userId") Long userId) {
        return "当前用户Id: " + userId;
    }

    /**
     * 如果每一个需要登录的方法，都从 Session 中获得当前用户标识，并进行一些后续处理的话。
     * 没有必要在每一个方法内都复制粘贴相同的获取用户身份的逻辑，可以定义一个自定义注解 @LongRequired 到 userId 参数上，
     * 然后通过 HandlerMethodArgumentResolver 自动实现参数的组装。
     */
    @RequestMapping(value = "/right")
    public String right(@LongRequired Long userId) {
        return "当前用户Id: " + userId;
    }

    /**
     * 登录后用户标识存在服务端，接口需要从服务端（比如 Session 中）获取。
     */
    @RequestMapping(value = "/login")
    public Long login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpSession session) {
        if (username.equals("admin") && password.equals("admin")) {
            session.setAttribute("currentUser", 1L);
            return 1L;
        }
        return 0L;
    }
}
