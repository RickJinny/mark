package com.rickjinny.mark.controller.p22_apidesign.t03_headerapiversion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/apiVersion")
@ApiVersion("v1")
public class ApiVersionController {

    @GetMapping(value = "/api/user")
    public int version1() {
        return 1;
    }

    @GetMapping(value = "/api/user")
    @ApiVersion("v2")
    public int version2() {
        return 2;
    }
}
