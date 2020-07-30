package com.rickjinny.mark.controller.p19_spring_01.t02_AopMetrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/metrics") // 自动进行监控
@Metrics(logParameters = false, logReturn = false)
public class MetricsController {

    @Autowired
    private UserService userService;


    public int transaction(@RequestParam("name") String name) {
        try {
            userService.createUser(new UserEntity(name));
        } catch (Exception e) {
            log.error("create user failed because {}", e.getMessage());
        }
        return userService.getUserCount(name);
    }
}
