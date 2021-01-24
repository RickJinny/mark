package com.rickjinny.mark.controller.p04_connectionpool.t02_httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClientNotReuseController {

    private static CloseableHttpClient httpClient;

    static {
        httpClient = HttpClients.custom()
                .setMaxConnPerRoute(1)
                .setMaxConnTotal(1)
                .evictIdleConnections(60, TimeUnit.SECONDS)
                .build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                httpClient.close();
            } catch (IOException e) {
                // ignore
            }
        }));
    }

    @GetMapping("/wrong1")
    public String wrong1() {
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .evictIdleConnections(60, TimeUnit.SECONDS)
                .build();
        try (CloseableHttpResponse response = client.execute(new HttpGet("http://127.0.0.1:8080/httpclientnotreuse/test"))) {
            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @GetMapping("/wrong2")
    public String wrong2() {
        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .evictIdleConnections(60, TimeUnit.SECONDS)
                .build();
        try (CloseableHttpResponse response = closeableHttpClient.execute(new HttpGet("http://127.0.0.1:8080/httpclientnotreuse/test"))) {
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/right")
    public String right() {
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet("http://127.0.0.1:8080/httpclientnotreuse/test"))) {
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}
