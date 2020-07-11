package com.rickjinny.mark.controller.p24_productionready.health;

import com.rickjinny.mark.controller.p24_productionready.health.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserServiceController {

    @GetMapping("/getUser")
    public User getUser(@RequestParam("userId") Long id) {
        // 一半概率返回正确响应，一半概率抛异常
        if (ThreadLocalRandom.current().nextInt() % 2 == 0) {
            return new User(id, "name" + id);
        } else {
            throw new RuntimeException("error");
        }
    }

    @GetMapping("/slowTask")
    public void slowTask() {
        ThreadPoolProvider.getDemoThreadPool().execute(() -> {
            try {
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
