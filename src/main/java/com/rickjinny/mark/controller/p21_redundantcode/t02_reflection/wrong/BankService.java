package com.rickjinny.mark.controller.p21_redundantcode.t02_reflection.wrong;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BankService {

    /**
     * 创建用户方法
     */
    public static String createUser(String name, String identity, String mobile, int age) throws IOException {
        StringBuilder sb = new StringBuilder();
        // 字符串靠左, 多余的地方填充_
        sb.append(String.format("%-10s", name).replace(' ', '_'));
        // 字符串靠左, 多余的地方填充_
        sb.append(String.format("%-18s", identity).replace(' ', '_'));
        // 数字靠右, 多余的地方用 0 填充
        sb.append(String.format("%05d", age));
        // 字符串靠左, 多余的地方用_填充
        sb.append(String.format("%-11s", mobile).replace(' ', '_'));
        // 最后加上 MD5 作为签名
        sb.append(DigestUtils.md2Hex(sb.toString()));
        return Request.Post("http://localhost:8080/reflection/bank/createUser")
                .bodyString(sb.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
    }

    /**
     * 支付方法
     */
    public static String pay(Long userId, BigDecimal amount) throws IOException {
        StringBuilder sb = new StringBuilder();
        // 数字靠右，多余的地方用 0 填充
        sb.append(String.format("%020d", userId));
        // 金额向下舍入 2 位到分, 以分为单位, 作为数字靠右, 多余的地方用 0 填充
        sb.append(String.format("%010d", amount.setScale(2, RoundingMode.DOWN)
                .multiply(new BigDecimal("100")).longValue()));
        // 最后加上 MD5 作为签名
        sb.append(DigestUtils.md2Hex(sb.toString()));
        return Request.Post("http://localhost:45678/reflection/bank/pay")
                .bodyString(sb.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
    }
}
