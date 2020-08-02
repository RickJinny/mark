package com.rickjinny.mark.controller.p27_clientdata.t03_TrustClienTip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/trustClientIp")
@Slf4j
public class TrustClientIpController {

    Set<String> activityLimit = new HashSet<>();

    @RequestMapping(value = "/test")
    public String test(HttpServletRequest request) {
        String ip = getClientIp(request);
        if (activityLimit.contains(ip)) {
            return "您已经领取过奖品";
        }
        activityLimit.add(ip);
        return "奖品领取成功";
    }

    private String getClientIp(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header == null) {
            return request.getRemoteAddr();
        }
        return header.contains(",") ? header.split(",")[0] : header;
    }
}
