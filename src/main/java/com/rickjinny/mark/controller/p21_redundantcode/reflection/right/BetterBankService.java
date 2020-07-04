package com.rickjinny.mark.controller.p21_redundantcode.reflection.right;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;

@Slf4j
public class BetterBankService {

    private static String remoteCall(AbstractAPI api) throws IOException {
        // 从 BankAPI 注解获取请求地址
        BankAPI bankAPI = api.getClass().getAnnotation(BankAPI.class);
        bankAPI.url();

        StringBuilder sb = new StringBuilder();
        // 获得所有字段
        Field[] declaredFields = api.getClass().getDeclaredFields();
        Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(BankAPIField.class)) // 查找标记了注解的字段
                .sorted(Comparator.comparingInt(a -> a.getAnnotation(BankAPIField.class).order())) // 根据注解中的 order 对字段排序
                .peek(field -> field.setAccessible(true)) // 设置可以访问私有字段
                .forEach(field -> {
                    // 获得注解
                    BankAPIField bankAPIField = field.getAnnotation(BankAPIField.class);
                    Object value = "";
                    try {
                        // 反射获取字段值
                        value = field.get(api);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 根据字段类型以正确的填充方式格式化字符串
                    switch (bankAPIField.type()) {
                        case "S":
                            sb.append(String.format("%-" + bankAPIField.length() + "s", value.toString()).replace(' ', '_'));
                            break;
                        case "N":
                            sb.append(String.format("%" + bankAPIField.length() + "s", value.toString()).replace(' ', '0'));
                            break;
                        case "M":
                            if (!(value instanceof BigDecimal)) {
                                throw new RuntimeException(String.format("{} 的 {} 必须是 BigDecimal", api, field));
                            }
                            sb.append(String.format("%0" + bankAPIField.length() + "d",
                                    ((BigDecimal) value).setScale(2, RoundingMode.DOWN)
                                            .multiply(new BigDecimal("100"))
                                            .longValue()));
                            break;
                        default:
                            break;
                    }
                });
        // 签名逻辑
        sb.append(DigestUtils.md2Hex(sb.toString()));
        String param = sb.toString();
        long begin = System.currentTimeMillis();
        // 发请求
        String result = Request.Post("http://localhost:8080/reflection" + bankAPI.url())
                .bodyString(param, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        log.info("调用银行API {}, url:{}, 参数:{}, 耗时: {}ms", bankAPI.desc(), bankAPI.url(), param, System.currentTimeMillis() - begin);
        return result;
    }
}
