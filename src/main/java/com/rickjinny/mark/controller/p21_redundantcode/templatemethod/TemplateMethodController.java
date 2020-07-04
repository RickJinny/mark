package com.rickjinny.mark.controller.p21_redundantcode.templatemethod;

import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.wrong.NormalUserCart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.Cart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.bean.DB;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.right.AbstractCart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.wrong.InternalUserCart;
import com.rickjinny.mark.controller.p21_redundantcode.templatemethod.wrong.VipUserCart;
import lombok.extern.slf4j.Slf4j;
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

    static {
        items.put(1L, 2);
        items.put(2L, 4);
    }

    private ApplicationContext applicationContext;

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
        String userCategory = DB.getUserCategory(userId);
        AbstractCart cart = (AbstractCart) applicationContext.getBean(userCategory + "userCategory");
        return cart.process(userId, items);
    }
}
