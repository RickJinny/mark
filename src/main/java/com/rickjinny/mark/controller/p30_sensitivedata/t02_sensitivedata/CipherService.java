package com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class CipherService {
    // 密钥长度
    public static final int AES_KEY_SIZE = 256;
    // 初始化向量长度
    private static final int GCM_IV_LENGTH = 12;
    // GCM身份认证 Tag 长度
    public static final int GCM_TAG_LENGTH = 16;

    @Autowired
    private CipherRepository cipherRepository;

    /**
     * 内部加密方法
     */
    public static byte[] doEncrypt(byte[] plainText, SecretKey key, byte[] iv, byte[] add) throws Exception {
        // 加密算法
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        // key 规范
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        // GCM 参数规范
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        // 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
        // 设置 aad
        if (add != null) {
            cipher.updateAAD(add);
        }
        // 加密
        byte[] cipherText = cipher.doFinal(plainText);
        return cipherText;
    }

    /**
     * 内部解密方法
     */
    public static String doDecrypt(byte[] cipherText, SecretKey key, byte[] iv, byte[] aad) throws Exception {
        // 加密算法
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        // key 规范
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        // GCM 参数规范
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        // 解密模式
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
        // 设置 aad
        if (aad != null) {
            cipher.updateAAD(aad);
        }
        // 解密
        byte[] decryptedText = cipher.doFinal(cipherText);
        return new String(decryptedText);
    }
}
