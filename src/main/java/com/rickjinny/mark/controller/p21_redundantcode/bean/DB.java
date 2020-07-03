package com.rickjinny.mark.controller.p21_redundantcode.bean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DB {

    private static Map<Long, BigDecimal> items = new HashMap<>();

    static {
        items.put(1L, new BigDecimal("10"));
        items.put(2L, new BigDecimal("20"));
    }

    public static BigDecimal getItemPrice(long id) {
        return items.get(id);
    }

    public static String getUserCategory(long userId) {
        if (userId == 1L) return "Normal";
        if (userId == 2L) return "Vip";
        if (userId == 3L) return "Internal";
        return "Normal";
    }

    public static int getUserCouponPercent(long userId) {
        return 90;
    }
}
