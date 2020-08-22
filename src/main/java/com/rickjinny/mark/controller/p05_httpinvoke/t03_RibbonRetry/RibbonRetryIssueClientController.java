package com.rickjinny.mark.controller.p05_httpinvoke.t03_RibbonRetry;

import com.rickjinny.mark.controller.p05_httpinvoke.t03_RibbonRetry.feign.SmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/ribbonRetryIssueClient")
public class RibbonRetryIssueClientController {

    @Autowired
    private SmsClient smsClient;

    @RequestMapping(value = "/wrong")
    public String wrong() {
        log.info("client is called");
        try {
            // 通过 Feign 调用发送短信接口
            smsClient.sendSmsWrong("13800000000", UUID.randomUUID().toString());
        } catch (Exception e) {
            // 捕获可能出现的网络错误
            log.error("send sms failed: {}", e.getMessage());
        }
        return "done";
    }

    @RequestMapping(value = "/right")
    public String right() {
        try {
            smsClient.sendSmsWrong("13800000000", UUID.randomUUID().toString());
        } catch (Exception e) {
            log.error("send sms failed : {} ", e.getMessage());
        }
        return "done";
    }
}
