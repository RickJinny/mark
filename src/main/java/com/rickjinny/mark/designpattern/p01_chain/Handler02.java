package com.rickjinny.mark.designpattern.p01_chain;

public class Handler02 extends Handler {

    @Override
    public void handleMessage(int type) {
        if (type == 2 || type == 3) {
            System.out.println("Handler02 解决了问题.");
        } else {
            System.out.println("Handler2 解决不了问题.");
            if (nextHandler != null) {
                nextHandler.handleMessage(type);
            } else {
                System.out.println("没有人能处理这个消息.");
            }
        }
    }
}
