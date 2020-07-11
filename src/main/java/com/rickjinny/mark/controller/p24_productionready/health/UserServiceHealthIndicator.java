package com.rickjinny.mark.controller.p24_productionready.health;

import com.rickjinny.mark.controller.p24_productionready.health.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class UserServiceHealthIndicator implements HealthIndicator {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 在 health 方法中，我们通过 RestTemplate 来访问这个 user 接口，如果结果正确则返回 Health.up()，
     * 并把调用执行耗时和结果作为补充信息加入 Health 对象中。
     * 如果调用接口出现异常，则返回 Health.down()，并把异常信息作为补充信息加入 Health 对象中。
     */
    @Override
    public Health health() {
        long begin = System.currentTimeMillis();
        long userId = 1L;
        User user;
        try {
            // 访问远程接口
            user = restTemplate.getForObject("http://localhost:45678/user?userId=" + userId, User.class);
            if (user != null && user.getUserId() == userId) {
                // 如果正确，返回 up 状态，补充提供耗时和用户信息
                return Health.up()
                        .withDetail("user", user)
                        .withDetail("took", System.currentTimeMillis() - begin)
                        .build();
            } else {
                // 结果不正确，返回 Down 状态，补充提供耗时。
                return Health.down().withDetail("took", System.currentTimeMillis() - begin).build();
            }
        } catch (Exception e) {
            // 出现异常，先记录异常，然后返回 DOWN 状态，补充提供异常信息和耗时
            log.warn("health check failed!", e);
            return Health.down(e).withDetail("took", System.currentTimeMillis() - begin).build();
        }
    }
}
