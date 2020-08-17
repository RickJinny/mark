package com.rickjinny.mark.controller.p05_httpinvoke.t01_ClientReadTimeout;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/clientReadTimeout")
@Slf4j
public class ClientReadTimeoutController {

    private String getResponse(String url, int connectTimeout, int readTime) throws IOException {
        return Request.Get("http://localhost:8080/clientreadtimeout" + url)
                .connectTimeout(connectTimeout)
                .socketTimeout(readTime)
                .execute()
                .returnContent()
                .asString();
    }
}
