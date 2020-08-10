package com.rickjinny.mark.controller.p08_equals.t02_equalsMethod;

import com.rickjinny.mark.controller.p08_equals.t02_equalsMethod.bean.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/equalsMethod")
public class EqualsMethodController {

    /**
     * 定义三个点 point1、point2、point3，其中 point1 和 point2 的描述属性不同,
     * point1 和 point3 的三个属性完全相同。
     */
    public static void wrong() {
        Point point1 = new Point(1, 2, "a");
        Point point2 = new Point(1, 2, "b");
        Point point3 = new Point(1, 2, "a");
        System.out.println(point1.equals(point2)); // false
        System.out.println(point1.equals(point3)); // false
    }

    public static void main(String[] args) {
        wrong();
    }
}
