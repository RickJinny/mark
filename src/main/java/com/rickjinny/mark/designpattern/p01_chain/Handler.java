package com.rickjinny.mark.designpattern.p01_chain;

/**
 * 抽象处理者
 */
public abstract class Handler {

    /**
     * 下一个责任链成员
     */
    protected Handler nextHandler;

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 处理传递过来的时间
     */
    public abstract void handleMessage(int type);
}
