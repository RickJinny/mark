package com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

@RestController
@Slf4j
@RequestMapping(value = "/storeIdCard")
public class StoreIdCardController {

    // 密钥
    private static final String KEY = "secretkey1234567";

    /**
     * 测试 ECB 模式：
     * ECB 模式虽然简单，但是不安全，不推荐使用。
     */
    @RequestMapping(value = "/ecb")
    public void ecb() throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        test(cipher, null);
    }

    /**
     * 获取加密密钥帮助方法
     */
    private static SecretKeySpec setKey(String secret) {
        return new SecretKeySpec(secret.getBytes(), "AES");
    }

    /**
     * 测试逻辑
     * 两个相同明文分组产生的密文，就是两个相同的密文分组叠在一起。
     * 在不知道密钥的情况下，我们操纵密文实现了对明文数据的修改，对调了发送方账户和接收方账号。
     */
    private void test(Cipher cipher, AlgorithmParameterSpec parameterSpec) throws Exception {
        // 初始化 Cipher
        cipher.init(Cipher.ENCRYPT_MODE, setKey(KEY), parameterSpec);
        // 加密测试文本
        System.out.println("一次: " + Hex.encodeHexString(cipher.doFinal("adcdefg".getBytes())));
        // 加密重复一次的测试文本
        System.out.println("两次: " + Hex.encodeHexString(cipher.doFinal("1h121bbn21".getBytes())));

        // 下面测试是否可以通过操作密文，来操作明文
        // 发送方账号
        byte[] sender = "110000011111".getBytes();
        // 接收方账号
        byte[] receiver = "33289001111".getBytes();
        // 转账金额
        byte[] money = "199".getBytes();
        // 加密发送方账号
        System.out.println("发送方账号: " + Hex.encodeHexString(cipher.doFinal(sender)));
        // 加密接收方账号
        System.out.println("接收方账号: " + Hex.encodeHexString(cipher.doFinal(receiver)));
        // 加密金额
        System.out.println("金额: " + Hex.encodeHexString(cipher.doFinal(money)));
        byte[] result = cipher.doFinal(ByteUtils.concatAll(sender, receiver, money));
        // 加密三个数据
        System.out.println("完整数据: " + Hex.encodeHexString(result));
        byte[] hack = new byte[result.length];
        // 把密文前两段交换
        System.arraycopy(result, 16, hack, 0, 16);
        System.arraycopy(result, 0, hack, 16, 16);
        System.arraycopy(result, 32, hack, 32, 16);
        cipher.init(Cipher.DECRYPT_MODE, setKey(KEY), parameterSpec);
        // 尝试解密
        System.out.println("原始明文: " + new String(ByteUtils.concatAll(sender, receiver, money)));
        System.out.println("操作密文: " + new String(cipher.doFinal(hack)));
    }
}
