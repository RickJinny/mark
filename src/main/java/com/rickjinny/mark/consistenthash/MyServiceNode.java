package com.rickjinny.mark.consistenthash;

import java.util.Arrays;

public class MyServiceNode implements Node {
    private final String idc;
    private final String ip;
    private final int port;

    public MyServiceNode(String idc, String ip, int port) {
        this.idc = idc;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String getKey() {
        return idc + "-" + ip + ":" + port;
    }

    @Override
    public String toString() {
        return getKey();
    }

    public static void main(String[] args) {
        MyServiceNode node01 = new MyServiceNode("idc1", "127.0.0.1", 8080);
        MyServiceNode node02 = new MyServiceNode("idc1", "127.0.0.1", 8081);
        MyServiceNode node03 = new MyServiceNode("idc1", "127.0.0.1", 8082);
        MyServiceNode node04 = new MyServiceNode("idc1", "127.0.0.1", 8083);

        ConsistentHashRouter<MyServiceNode> consistentHashRouter = new ConsistentHashRouter<>(
                Arrays.asList(node01, node02, node03, node04), 10);

    }
}
