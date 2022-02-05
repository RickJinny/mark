package com.rick.test.util.netty;


public class NettyServerApplication {

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(false, 8089);
        try {
            httpServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
