package com.rickjinny.mark.controller.p16_datetime.t03_DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    /**
     * SimpleDateFormat 第一个坑
     *
     * 定义 static SimpleDateFormat 可能会带来线程安全问题。比如像这样，使用一个 100 线程的线程池，循环 20 次把时间格式化任务
     * 提交到线程池处理，每个任务中又循环 10 次解析 2020-01-01 11:22:33 这样一个时间表示。
     *
     * 运行程序后大量报错，没有报错的输出结果也不正常。
     *
     * SimpleDateFormat 的作用是：定义解析和格式化日期时间的模式。这看起来是一次性的工作，应该复用，但它的解析和格式化操作是非线程安全的。
     *
     * 经过源码的分析，format 方法，只能在同一个线程复用 SimpleDateFormat，比较好的解决方案是通过 ThreadLocal 来存放 SimpleDateFormat。
     */
    private static void wrong2() throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 20; i++) {
            // 提交 20 个并发解析时间的任务到线程池，模拟并发环境
            threadPool.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
                        System.out.println(simpleDateFormat.parse("2020-01-01 11:22:33"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    /**
     * 使用 ThreadLocal 来存放 SimpleDateFormat。
     */
    private static void right2() throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 20; i++) {
            // 提交 20 个并发解析时间的任务到线程池，模拟并发环境
            threadPool.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
                        System.out.println(threadSafeSimpleDateFormat.get().parse("2020-01-01 11:22:33"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }


    /**
     * SimpleDateFormat 第二坑是：当需要解析的字符串和格式不匹配时，SimpleDateFormat 表现得很宽容。
     *
     * 结果居然输出了 2091年1月1日，原始是把0901当成了月份，相当于是75年。
     *
     * result : Mon Jan 01 00:00:00 CST 2091
     */
    private static void wrong3() throws ParseException {
//        LocalDateTime localDateTime = LocalDateTime.parse("2020/1/2 12:09:28.789", dateTimeFormatter);
//        System.out.println(localDateTime.format(dateTimeFormatter));

        String dateTime = "20160901";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        System.out.println("result : " + dateFormat.parse(dateTime));
    }

    /**
     * SimpleDateFormat 第三坑，我们使用 Java8 中的 DateTimeFormatter 就可以避免过去。
     * 首先，使用 DateTimeFormatterBuilder 来定义格式化字符串，不用去记忆使用大写的 Y 还是小写的 y，大写的 M 还是小写的 m。
     *
     * 其次，DateTimeFormatter 是线程安全的，可以定义为 static 使用，最后 DateTimeFormatter 的解析比较严格，需要解析的字符串和格式不匹配时，
     * 会直接报错，而不会把 0901 解析为月份。
     */
    private static void wrong4() throws ParseException {
        // 使用刚才定义的 DateTimeFormatterBuilder 构建的 DateTimeFormatter 来解析这个时间
        LocalDateTime localDateTime = LocalDateTime.parse("2020/1/2 12:09:28.789", dateTimeFormatter);
        // 解析成功
        System.out.println(localDateTime.format(dateTimeFormatter));
        // 使用 yyyy 格式解析 20160901 是否可以成功呢？
        String dateTime = "20160901";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        System.out.println("result : " + dateFormat.parse(dateTime));
    }

    public static void main(String[] args) throws ParseException, InterruptedException {
//        wrong1();
//        right1();
//        wrong2();
//        right2();
        wrong3();
    }
}
