package com.rickjinny.mark.controller.p13_log.t01_async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/logging")
@RestController
public class LoggingController {

    @RequestMapping(value = "/log")
    public void log() {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }
}
