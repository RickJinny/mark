package com.rickjinny.mark.controller.p28_securitylastdefense.t01_PreventSmsHijack;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/preventSmsHijack")
public class PreventSmsHijackController {

    @RequestMapping(value = "/wrong")
    public void wrong() {
        sendSMS("13600000000");
    }

    /**
     * 考虑防刷的场景
     * 第一、只有固定的请求头，才能发送验证码。
     * 第二、只有先来到过注册页面，才能发送验证码。
     * 第三、控制相同手机号一天只能发送 10 次验证码。
     * 第四、控制相同手机号发送间隔 1 分钟。
     * 第五、同一个 IP 超过阈值，短信验证码需要图形验证码前置
     */
    @RequestMapping(value = "/right")
    public void right() {

        sendSMS("13600000000");
    }

    private void sendSMS(String mobile) {
        // 调用第三方短信通道
    }
}
