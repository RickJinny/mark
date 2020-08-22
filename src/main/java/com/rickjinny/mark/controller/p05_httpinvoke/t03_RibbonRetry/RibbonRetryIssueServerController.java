package com.rickjinny.mark.controller.p05_httpinvoke.t03_RibbonRetry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/ribbonRetryIssueServer")
public class RibbonRetryIssueServerController {

    @GetMapping(value = "/sms")
    public void sendSmsWrong(@RequestParam("mobile") String mobile,
                             @RequestParam("message") String message,
                             HttpServletRequest request) throws InterruptedException {
        // 输出调用参数后，休眠 2s
        log.info("{} is called. {} = {}", request.getRequestURL().toString(), mobile, message);
        TimeUnit.SECONDS.sleep(2);
    }

    @RequestMapping(value = "/sms")
    public void sendSmsRight(@RequestParam("mobile") String mobile,
                             @RequestParam("message") String message,
                             HttpServletRequest request) throws InterruptedException {
        log.info("{} is called, {} => {}", request.getRequestURL().toString(), mobile, message);
        TimeUnit.SECONDS.sleep(2);
    }
}
