package com.rickjinny.mark.controller.p16_datetime.t01_NewDate;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NewDateApplication {

    /**
     * 打印结果：Sat Jan 31 10:20:30 CST 3920
     */
    private static void wrong1() {
        System.out.println("wrong1");
        Date date = new Date(2019, 12, 31, 10, 20, 30);
        System.out.println(date);
    }

    /**
     * Fri Jan 31 10:20:30 CST 2020
     */
    private static void right1() {
        System.out.println("right1");
        Date date = new Date(2019 - 1900, 12, 31, 10, 20, 30);
        System.out.println(date);
    }

    /**
     * Tue Dec 31 10:20:30 CST 2019
     * Tue Dec 31 23:20:30 CST 2019
     *
     * 输出显示了两个时间，说明时区产生了作用。但我们更习惯 年/月/日 时:分:秒 这样的日期时间格式
     */
    private static void right2() {
        System.out.println("right2");

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2019, 11, 31, 10, 20, 30);
        System.out.println(calendar1.getTime());

        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        calendar2.set(2019, Calendar.DECEMBER, 31, 10, 20, 30);
        System.out.println(calendar2.getTime());
    }

    /**
     * 更好的处理方法
     *
     * 结果是
     * 2019-12-31T10:20:30
     * 2019-12-31T10:20:30-05:00[America/New_York]
     */
    private static void better() {
        System.out.println("better");
        LocalDateTime localDateTime = LocalDateTime.of(2019, Month.DECEMBER, 31, 10, 20, 30);
        System.out.println(localDateTime);

        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("America/New_York"));
        System.out.println(zonedDateTime);
    }

    public static void main(String[] args) {
        wrong1();
        right1();
        right2();
        better();
    }
}
