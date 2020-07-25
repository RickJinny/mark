package com.rickjinny.mark.controller.p16_datetime.t03_DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Locale;

/**
 * 3、日期时间格式化和解析
 * 每年年底，就有很多开发同学踩时间格式化的坑，比如："这明明是一个 2019 年的日期，怎么使用 SimpleDateFormat 格式化后就提前跨年了"。
 */
public class DateFormatApplication {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static ThreadLocal<SimpleDateFormat> threadSafeSimpleDateFormat = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private static DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR)
            .appendLiteral("/")
            .appendValue(ChronoField.MONTH_OF_YEAR)
            .appendLiteral("/")
            .appendValue(ChronoField.DAY_OF_MONTH)
            .appendLiteral(" ")
            .appendValue(ChronoField.HOUR_OF_DAY)
            .appendLiteral(":")
            .appendValue(ChronoField.MINUTE_OF_HOUR)
            .appendLiteral(":")
            .appendValue(ChronoField.SECOND_OF_MINUTE)
            .appendLiteral(".")
            .appendValue(ChronoField.MILLI_OF_SECOND)
            .toFormatter();


    /**
     * 初始化一个 Calendar，设置日期时间为 2019年12月29日，使用大写的 YYYY 来初始化 SimpleDateFormat
     * <p>
     * default Locale: fr_FR
     * 格式化: 2020-12-29
     * weekYear: 2020
     * first Day of Week: 2
     * minimal Days in First Week: 4
     * result: Thu Jan 01 00:00:00 CST 2093
     */
    private static void wrong1() throws ParseException {
        // 三个问题：YYYY、线程不变安全、不合法格式
        Locale.setDefault(Locale.FRANCE);
        System.out.println("default Locale: " + Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.DECEMBER, 29, 0, 0, 0);
        SimpleDateFormat YYYY = new SimpleDateFormat("YYYY-MM-dd");
        System.out.println("格式化: " + YYYY.format(calendar.getTime()));
        System.out.println("weekYear: " + calendar.getWeekYear());
        System.out.println("first Day of Week: " + calendar.getFirstDayOfWeek());
        System.out.println("minimal Days in First Week: " + calendar.getMinimalDaysInFirstWeek());

        String dateString = "20180901";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        System.out.println("result: " + dateFormat.parse(dateString));
    }

    /**
     * 格式化: 2020-07-20
     * result: Sat Sep 26 00:00:00 CST 2020
     */
    private static void right1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JULY, 20, 10, 20, 30);
        System.out.println("格式化: " + sdf.format(calendar.getTime()));

        String dateString = "20200926";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        System.out.println("result: " + dateFormat.parse(dateString));
    }

    private static void better() {
        LocalDateTime localDateTime = LocalDateTime.parse("2020/1/2 12:09:28.789", dateTimeFormatter);
        System.out.println(localDateTime.format(dateTimeFormatter));

        String dateTime = "20160901";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        System.out.println("result : " + dateTimeFormatter.parse(dateTime));
    }

    public static void main(String[] args) throws ParseException {
//        wrong1();
        right1();
        better();
    }
}
