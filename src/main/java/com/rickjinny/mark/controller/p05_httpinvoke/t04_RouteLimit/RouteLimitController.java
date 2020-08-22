package com.rickjinny.mark.controller.p05_httpinvoke.t04_RouteLimit;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping(value = "/routeLimit")
public class RouteLimitController {
    private static CloseableHttpClient httpClient1;
    private static CloseableHttpClient httpClient2;

    static {
        httpClient1 = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
        httpClient2 = HttpClients.custom().setMaxConnPerRoute(10).setMaxConnTotal(20).build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                httpClient1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                httpClient2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private int sendRequest(int count, Supplier<CloseableHttpClient> client) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        ExecutorService threadPool = Executors.newCachedThreadPool();
        long beginTime = System.currentTimeMillis();
        IntStream.rangeClosed(1, count).forEach(i -> {
            threadPool.execute(() -> {
                try (CloseableHttpResponse response = client.get().execute(new HttpGet("http://127.0.0.1:45678/routelimit/server"))) {
                    atomicInteger.addAndGet(Integer.parseInt(EntityUtils.toString(response.getEntity())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
        log.info("发送 {} 次请求，耗时 {} ms", atomicInteger.get(), System.currentTimeMillis() - beginTime);
        return atomicInteger.get();
    }

    @RequestMapping(value = "/wrong")
    public int wrong(@RequestParam(value = "count", defaultValue = "10") int count) throws InterruptedException {
        return sendRequest(count, () -> httpClient1);
    }

    public int right(@RequestParam(value = "count", defaultValue = "10") int count) throws InterruptedException {
        return sendRequest(count, () -> httpClient2);
    }

    @RequestMapping(value = "/server")
    public int server() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return 1;
    }
}
