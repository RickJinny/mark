package com.rickjinny.mark.controller.p08_equals.t02_equalsMethod;

import com.rickjinny.mark.controller.p08_equals.t02_equalsMethod.bean.Point;
import com.rickjinny.mark.controller.p08_equals.t02_equalsMethod.bean.PointWrong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
@Slf4j
@RequestMapping(value = "/equalsMethod")
public class EqualsMethodController {

    /**
     * 定义三个点 point1、point2、point3，其中 point1 和 point2 的描述属性不同,
     * point1 和 point3 的三个属性完全相同。
     * 分析：因为没有在 Point 类中实现自定义的 equals 方法，比较的是对象的引用。
     */
    public static void wrong1() {
        Point point1 = new Point(1, 2, "a");
        Point point2 = new Point(1, 2, "b");
        Point point3 = new Point(1, 2, "a");
        System.out.println(point1.equals(point2)); // false
        System.out.println(point1.equals(point3)); // false
    }

    public static void wrong2() {
        /**
         * 比较一个 Point 对象和 null
         */
        PointWrong p1 = new PointWrong(1, 2, "a");
        try {
            log.info("p1.equals(null) ? {}", p1.equals(null)); // java.lang.NullPointerException
        } catch (Exception e) {
            log.error(e.toString());
        }

        /**
         * 比较一个 Object 对象和一个 Point 对象
         */
        Object object = new Object();
        try {
            log.info("p1.equals(expression) ? {}", p1.equals(object)); // java.lang.ClassCastException
        } catch (Exception e) {
            log.error(e.toString());
        }

        /**
         * 比较两个 x 和 y 属性值相同的 Point 对象
         */
        PointWrong p2 = new PointWrong(1, 2, "b");
        log.info("p1.equals(p2) ? {}", p1.equals(p2)); // p1.equals(p2) ? true

        HashSet<PointWrong> points = new HashSet<>();
        points.add(p1);
        log.info("points.contains(p2) ? {}", points.contains(p2)); // points.contains(p2) ? false
    }

    public static void main(String[] args) {
//        wrong1();
        wrong2();
    }
}
