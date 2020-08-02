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

    @RequestMapping(value = "/right")
    public String right(@LongRequired Long userId) {
        return "当前用户Id: " + userId;
    }

    /**
     * 
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
