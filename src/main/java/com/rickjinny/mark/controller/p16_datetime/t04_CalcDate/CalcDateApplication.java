package com.rickjinny.mark.controller.p16_datetime.t04_CalcDate;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 4、计算日期
 */
public class CalcDateApplication {

    /**
     * 第一个坑：直接使用时间戳进行时间计算，比如希望得到当前时间之后 30 天的时间，会这么写代码：直接把 new Date().getTime() 方法
     * 得到的时间戳加 30 天对应的毫秒数，也就是 30 天 * 1000 毫秒 * 3600 秒 * 24 小时
     *
     * 得到的结果，居然比当前日期还要早，根本不是晚 30 天的时间。
     * today : Sat Jul 25 15:57:58 CST 2020
     * nextMonth : Sun Jul 05 22:55:11 CST 2020
     *
     * 出现这个问题的原因是：int 发生了溢出。修复的方式是把 30 天改为 30L, 让其成为一个 Long。
     */
    private static void wrong1() {
        // 现在的时间
        Date today = new Date();
        // 下个月的时间
        Date nextMonth = new Date(today.getTime() + 30 * 1000 * 60 * 60 * 24);
        System.out.println("today : " + today);
        System.out.println("nextMonth : " + nextMonth);
    }

    /**
     * int 发生了溢出。修复的方式是把 30 天改为 30L, 让其成为一个 Long。
     *
     * 结果为：
     * Sat Jul 25 16:04:43 CST 2020
     * Mon Aug 24 16:04:43 CST 2020
     */
    private static void right1() {
        System.out.println(30 * 1000 * 60 * 60 * 24 + " " + (30L * 1000 * 60 * 60 * 24 > Integer.MAX_VALUE));
        System.out.println("right1");

        Date today = new Date();
        Date nextMonth = new Date(today.getTime() + 30L * 1000 * 60 * 60 * 24);
        System.out.println(today);
        System.out.println(nextMonth);
    }

    /**
     * 不难发现，手动在时间戳上进行计算操作的方式非常容易出错。对于 Java8 之前的代码，我更建议使用 Calendar。
     * Mon Aug 24 16:07:07 CST 2020
     */
    private static void right2() {
        System.out.println("right");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        System.out.println(calendar.getTime());
    }

    /**
     * 使用 Java8 的日期类型，可以直接进行各种计算，更加简洁和方便。
     * 2020-08-24T16:10:06.664
     */
    private static void right3() {
        System.out.println("right3");
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime nextMonth = today.plusDays(30);
        System.out.println(nextMonth);
    }

    /**
     * 对于日期时间做计算操作，Java8 日期时间 api 会比 Calendar 功能强大很多。
     */
    private static void test() {
        System.out.println("测试操作日期");

        /**
         * 第一、可以使用各种 minus 和 plus 方法直接对日期进行加减操作，
         * 比如如下代码实现了减一天和加一天，以及减一天和加一个月。
         */
        LocalDate date1 = LocalDate.now()
                .minus(Period.ofDays(1))
                .plus(1, ChronoUnit.DAYS)
                .minusMonths(1)
                .plus(Period.ofMonths(1));
        System.out.println(date1);

        /**
         * 第二、还可以通过 with 方法进行快捷时间调节，比如：
         * 使用 TemporalAdjusters.firstDayOfMonth() 得到当前月的第一天;
         * 使用 TemporalAdjusters.firstDayOfYear() 得到当前年的第一天;
         * 使用 TemporalAdjusters.previous(DayOfWeek.SATURDAY) 得到上一个周六;
         * 使用 TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY) 得到本月最后一个周五;
         */
        LocalDate localDate01 = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        System.out.println("本月的第一天: " + localDate01);

        LocalDate localDate02 = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).plusDays(255);
        System.out.println("今年的程序员日: " + localDate02);

        LocalDate localDate03 = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SATURDAY));
        System.out.println("今天之前的一个周六: " + localDate03);

        LocalDate localDate04 = LocalDate.now().with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));
        System.out.println("本月的最后一个工作日: " + localDate04);

        // 直接使用 Lambda 表达式进行自定义的时间调整。比如：为当前时间增加 100 天以内的随机天数。
        LocalDate localDate05 = LocalDate.now().with(temporal ->
                temporal.plus(ThreadLocalRandom.current().nextInt(100), ChronoUnit.DAYS));
        System.out.println("自定义逻辑: " + localDate05);

        Boolean query = LocalDate.now().query(CalcDateApplication::isFamilyBirthday);
        System.out.println("查询是否是今天要举办生日: " + query);

        /**
         * Java8 虽然简单，但是容易采坑。
         * Java8 中有一个专门的类 Period 定义了日期间隔，通过 Period.between 得到了两个 LocalDate 的差，返回的是两个日期差几年零几个月零几天。
         * 如果希望得知两个日期之间差几天，直接调用 Period 的 getDays() 方法得到的只是最后的 "零几天"，而不是算总的间隔天数。
         *
         * 比如：计算2019年12月12日和2019年10月1日的日期间隔，很明显日期差是2个月零11天，但获取 getDays 方法得到的结果只是11天，而不是72天。
         */
        System.out.println("计算日期差");
        LocalDate today = LocalDate.of(2019, 12, 12);
        LocalDate specifyDate = LocalDate.of(2019, 10, 1);
        System.out.println(Period.between(specifyDate, today).getDays());
        System.out.println(Period.between(specifyDate, today));
        System.out.println(ChronoUnit.DAYS.between(specifyDate, today));
    }

    /**
     * 判断日期是否符合条件。比如自定义函数，判断指定日期是否是家庭成员的生日。
     */
    private static boolean isFamilyBirthday(TemporalAccessor date) {
        int month = date.get(ChronoField.MONTH_OF_YEAR);
        int day = date.get(ChronoField.DAY_OF_MONTH);

        if (month == Month.FEBRUARY.getValue() && day == 17) {
            return Boolean.TRUE;
        } else if (month == Month.SEPTEMBER.getValue() && day == 21) {
            return Boolean.TRUE;
        } else if (month == Month.MAY.getValue() && day == 22) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static void main(String[] args) {
//        wrong1();
//        right1();
//        right2();
//        right3();
        test();
    }
}
