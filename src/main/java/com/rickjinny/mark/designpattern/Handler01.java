package com.rickjinny.mark.designpattern;

/**
 * 在当前处理者对象无法处理时，将执行权传给下一个处理者对象
 */
public class Handler01 extends Handler {

    @Override
    public void handleMessage(int type) {
        if (type == 1 || type == 3) {
            System.out.println("Handler01 解决了问题.");
        } else {
            System.out.println("");
            if (nextHandler != null) {
                nextHandler.handleMessage(type);
            } else {
                System.out.println("没有人能处理这个消息.");
            }
        }
    }
}
