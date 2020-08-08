package com.rickjinny.mark.controller.p30_sensitivedata.t03_RsavsAes;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@Slf4j
@RequestMapping(value = "/rsaVsAes")
public class RsaVsAesController {

    private static KeyPair rsaKeyPair = genRSAKeyPair(2048);

    private static SecretKey aesKey = genAESKey(256);

    private static byte[] aesIv = genAesIv(16);

    private static int count = 100_000;


    private static void aes(byte[] data) {
        byte[] encryptAES = encryptAES(data, aesKey, aesIv);
        byte[] decryptAES = decryptAES(encryptAES, aesKey, aesIv);
        Assert.assertTrue(Arrays.equals(decryptAES, data));
    }

    private static void rsa(byte[] data) {
        PublicKey publicKey = rsaKeyPair.getPublic();
        PrivateKey privateKey = rsaKeyPair.getPrivate();
        byte[] encryptRSABytes = encryptRSA(data, publicKey);
        byte[] decryptRSABytes = decryptRSA(encryptRSABytes, privateKey);
        Assert.assertTrue(Arrays.equals(decryptRSABytes, data));
    }

    private static KeyPair genRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keyLength);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] genAesIv(int ivLength) {
        byte[] iv = new byte[ivLength];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    private static SecretKey genAESKey(int keyLength) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(keyLength);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decryptAES(byte[] content, SecretKey secretKey, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encryptAES(byte[] content, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encryptRSA(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decryptRSA(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("RSA");
        IntStream.rangeClosed(1, count).parallel().forEach(i -> rsa((UUID.randomUUID().toString() + IntStream.rangeClosed(1, 64)
                .mapToObj(__ -> "a").collect(Collectors.joining(""))).getBytes()));
        stopWatch.stop();

        stopWatch.start("aes");
        IntStream.rangeClosed(1, count).parallel().forEach(i -> aes((UUID.randomUUID().toString() +
                IntStream.rangeClosed(1, 64).mapToObj(__ -> "a").collect(Collectors.joining(""))).getBytes()));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
