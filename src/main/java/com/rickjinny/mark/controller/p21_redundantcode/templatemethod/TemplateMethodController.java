package com.rickjinny.mark.controller.p21_redundantcode.templatemethod;

import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.wrong.NormalUserCart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.Cart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.DB;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.right.AbstractCart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.wrong.InternalUserCart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.wrong.VipUserCart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/templateMethod")
public class TemplateMethodController {

    private static Map<Long, Integer> items = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    static {
        items.put(1L, 2);
        items.put(2L, 4);
    }

    @GetMapping("/wrong")
    public Cart wrong(@RequestParam("userId") Long userId) {
        // 根据用户id 获得用户类型
        String userCategory = DB.getUserCategory(userId);
        // 普通用户处理逻辑
        if (userCategory.equals("Normal")) {
            NormalUserCart normalUserCart = new NormalUserCart();
            return normalUserCart.process(userId, items);
        }
        // VIP用户处理逻辑
        if (userCategory.equals("Vip")) {
            VipUserCart vipUserCart = new VipUserCart();
            return vipUserCart.process(userId, items);
        }
        // 内部用户处理逻辑
        if (userCategory.equals("Internal")) {
            InternalUserCart internalUserCart = new InternalUserCart();
            return internalUserCart.process(userId, items);
        }

        return null;
    }

    @GetMapping("/right")
    public Cart right(@RequestParam("userId") Long userId) {
        /**
         * 在定义三个购物车子类时, 我们在 @Service 注解中对 Bean 进行了命名。既然三个购物车都叫 XXXUserCart，那我们就可以
         * 把用户类型字符串拼接 UserCart 构成购物车 Bean 的名称，然后利用 Spring 的 IOC 容器，通过 Bean 的名称直接获取到
         * AbstractCart, 调用 process 方法即可实现通用。
         * 其实这就是工厂模式, 只不过借助 Spring 容器实现罢了。
         */
        // 把用户类型字符串拼接 UserCart 构成购物车 Bean 的名称
        String userCategory = DB.getUserCategory(userId);
        // 利用 Spring 的 IOC 容器，通过 Bean 的名称直接获取到 AbstractCart 对象
        AbstractCart cart = (AbstractCart) applicationContext.getBean(userCategory + "UserCart");
        // AbstractCart 对象，调用 process 方法即可实现通用
        return cart.process(userId, items);
    }
}
