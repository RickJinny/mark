package com.rickjinny.mark.controller.p19_spring.t01_BeanSingletonAndOrder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 在 SayHello 和 SayBye 类上加了 @Service 注解，让他们成为 Bean, 也没有考虑到父类是有状态的。
 *
 * 正确的处理方式：在为类标记上 @Service 注解，把类型交由容器管理前，首先评估一下类是否有状态，然后为 Bean 设置合适的 Scope。
 * 将该类设置生命周期 value = ConfigurableBeanFactory.SCOPE_PROTOTYPE。
 */
@Service
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SayHello extends SayService {

    @Override
    public void say() {
        super.say();
    }
}
