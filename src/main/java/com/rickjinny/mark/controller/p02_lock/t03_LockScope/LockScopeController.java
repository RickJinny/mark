package com.rickjinny.mark.controller.p02_lock.t03_LockScope;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/lockScope")
public class LockScopeController {

    @RequestMapping(value = "/wrong1")
    public void wrong1() {
        Interesting interesting = new Interesting();
        new Thread(() -> interesting.add()).start();
        new Thread(() -> interesting.compare()).start();
        
    }
}
