package com.rickjinny.mark.controller.p03_threadpool.t02_ThreadPoolReuse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value = "/threadPoolReuse")
@Slf4j
public class ThreadPoolReuseController {

    @RequestMapping(value = "/wrong")
    public String wrong() {
        ThreadPoolExecutor threadPool = ThreadPoolHelper.getThreadPool();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            threadPool.execute(() -> {
                String payload = IntStream.rangeClosed(1, 1000000)
                        .mapToObj(__ -> "a")
                        .collect(Collectors.joining("")) + UUID.randomUUID().toString();
                
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {

                }
                log.debug(payload);
            });
        });
        return "OK";
    }
}
