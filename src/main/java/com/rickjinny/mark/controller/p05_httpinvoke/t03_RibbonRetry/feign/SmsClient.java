package com.rickjinny.mark.controller.p05_httpinvoke.t03_RibbonRetry.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SmsClient")
public interface SmsClient {

    @RequestMapping(value = "/ribbonRetryIssueServer/sms")
    void sendSmsWrong(@RequestParam("mobile") String mobile, @RequestParam("message") String message);

    @RequestMapping(value = "/ribbonRetryIssueServer/sms")
    void sendSmsRight(@RequestParam("mobile") String mobile, @RequestParam("message") String message);

}
