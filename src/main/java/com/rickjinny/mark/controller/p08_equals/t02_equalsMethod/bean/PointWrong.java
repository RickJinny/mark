package com.rickjinny.mark.controller.p08_equals.t02_equalsMethod.bean;

public class PointWrong {

    private int x;
    private int y;
    private final String desc;

    public PointWrong(int x, int y, String desc) {
        this.x = x;
        this.y = y;
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        PointWrong pointWrong = (PointWrong) o;
        return x == pointWrong.x && y == pointWrong.y;
    }
}
