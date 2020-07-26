package com.rickjinny.mark.controller.p17_oom.t03_ImproperMaxHeaderSize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 3、Tomcat 参数配置不合理导致 OOM
 */
@RestController
@Slf4j
@RequestMapping(value = "/improperMaxHeaderSize")
public class ImproperMaxHeaderSizeController {

    @Autowired
    private Environment env;

    @RequestMapping(value = "/oom")
    public String oom() {
        log.info("get request");
        log.info("server.max-http-header-size={},server.tomcat.max-threads={}",
                env.getProperty("server.max-http-header-size")
                , env.getProperty("server.tomcat.max-threads"));

        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "OK";
    }
}
