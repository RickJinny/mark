package com.rickjinny.mark.controller.p05_httpinvoke.t01_ClientReadTimeout;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/clientReadTimeout")
@Slf4j
public class ClientReadTimeoutController {

    private String getResponse(String url, int connectTimeout, int readTimeout) throws IOException {
        return Request.Get("http://localhost:8080/clientreadtimeout" + url)
                .connectTimeout(connectTimeout)
                .socketTimeout(readTimeout)
                .execute()
                .returnContent()
                .asString();
    }

    @GetMapping(value = "/client")
    public String client() throws IOException {
        log.info("client1 called");
        // 服务端 5s 超时，客户端读取超时 2s
        return getResponse("/server?timeout=5000", 1000, 2000);
    }

    @RequestMapping(value = "/server")
    public void server(@RequestParam("timeout") int timeout) throws InterruptedException {
        log.info("server called");
        TimeUnit.MILLISECONDS.sleep(timeout);
        log.info("Done");
    }
}
