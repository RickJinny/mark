package com.rickjinny.mark.controller.p16_datetime.t02_TimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * 2、"恼人" 的时区问题
 */
public class TimeZoneApplication {

    private static void test() {
        System.out.println("test");
        System.out.println(new Date(0));
        System.out.println(TimeZone.getDefault().getID() + ":" + TimeZone.getDefault().getRawOffset() / 3600 / 1000);
        ZoneId.getAvailableZoneIds().forEach(id -> System.out.println(String.format("%s:%s", id, ZonedDateTime.now(ZoneId.of(id)))));
    }

    /**
     * 第一类问题是：对于同一时间表示，比如 2020-01-02 22:00:00, 不同时区的人转换成 Date 会得到不同的时间（时间戳）
     *
     * Thu Jan 02 22:00:00 CST 2020:1577973600000
     * Fri Jan 03 11:00:00 CST 2020:1578020400000
     */
    private static void wrong1() throws ParseException {
        System.out.println("wrong1");
        String stringDate = "2020-01-02 22:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 默认时区解析时间表示
        Date date1 = sdf.parse(stringDate);
        System.out.println(date1 + ":" + date1.getTime());

        // 纽约时区解析时间表示
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Date date2 = sdf.parse(stringDate);
        System.out.println(date2 + ":" + date2.getTime());
    }

    /**
     * 第二类问题是：格式化后出现错乱，即同一个 Date，在不同的时区下格式化得到不同的时间表示，
     * 比如在我的当前时区和纽约时区格式化 2020-01-02 22:00:00
     *
     * [2020-02-02 22:00:00 +0800]
     * [2020-02-02 09:00:00 -0500]
     */
    private static void wrong2() throws ParseException {
        System.out.println("wrong2");
        String stringDate = "2020-02-02 22:00:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(stringDate);
        System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss Z]").format(date));

        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss Z]").format(date));
    }

    /**
     * 正确的处理时区：
     * 在于存进去和读出来两个方面。存的时候，需要使用正确的当前时区来保存，这样 UTC 时间才会正确；
     * 读的时候，也只有正确设置本地时区，才能把 UTC 时间转换为正确的当地时间。
     *
     * Asia/Shanghai2020-01-02 21:00:00 +0800
     * America/New_York2020-01-02 08:00:00 -0500
     * +09:002020-01-02 22:00:00 +0900
     *
     * 要正确处理国际化时间问题，我推荐使用 Java8 的日期时间类，即使用 ZonedDateTime 保存时间，然后使用设置了 ZoneId 的 DateTimeFormatter
     * 配合 ZonedDateTime 进行时间格式化得到本地时间表示。这样的划分十分清晰、细化，也不容易出错。
     */
    private static void right() {
        System.out.println("right");
        String stringDate = "2020-01-02 22:00:00";
        ZoneId timeZoneSH = ZoneId.of("Asia/Shanghai");
        ZoneId timeZoneNY = ZoneId.of("America/New_York");
        ZoneOffset timeZoneJST = ZoneOffset.ofHours(9);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime date = ZonedDateTime.of(LocalDateTime.parse(stringDate, dateTimeFormatter), timeZoneJST);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        System.out.println(timeZoneSH.getId() + dtf.withZone(timeZoneSH).format(date));
        System.out.println(timeZoneNY.getId() + dtf.withZone(timeZoneNY).format(date));
        System.out.println(timeZoneJST.getId() + dtf.withZone(timeZoneJST).format(date));
    }

    public static void main(String[] args) throws ParseException {
//        test();
//        wrong1();
//        wrong2();
        right();
    }
}
