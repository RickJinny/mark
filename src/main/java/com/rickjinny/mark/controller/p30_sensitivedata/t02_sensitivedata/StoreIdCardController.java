package com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata;

import com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata.bean.CipherResult;
import com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata.bean.UserData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

@RestController
@Slf4j
@RequestMapping(value = "/storeIdCard")
public class StoreIdCardController {

    // 密钥
    private static final String KEY = "secretkey1234567";

    // 初始化向量
    private static final String initVector = "abcdefg";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CipherService cipherService;

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

    /**
     * CBC 模式
     * 对于敏感数据保存，可以选择使用 AES + 合适的模式 进行加密。
     */
    @RequestMapping(value = "/cbc")
    public void cbc() throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector.getBytes("UTF-8"));
        test(cipher, ivParameterSpec);
    }

    @RequestMapping(value = "/wrong")
    public UserData wrong(@RequestParam(value = "name", defaultValue = "haha") String name,
                          @RequestParam(value = "idcard", defaultValue = "1234") String idCard) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setIdCard(idCard);
        return userRepository.save(userData);
    }

    /**
     * 正确的加密
     */
    @RequestMapping(value = "/right")
    public UserData right(@RequestParam(value = "name", defaultValue = "haha") String name,
                          @RequestParam(value = "idcard", defaultValue = "30001000") String idCard,
                          @RequestParam(value = "aad", required = false) String aad) throws Exception {
        UserData userData = new UserData();
        userData.setId(1L);
        // 脱敏姓名
        userData.setName(chineseName(name));
        // 脱敏身份证
        userData.setIdCard(idCard(idCard));
        // 加密姓名
        CipherResult cipherResultName = cipherService.encrypt(name, aad);
        userData.setNameCipherId(cipherResultName.getId());
        userData.setNameCipherText(cipherResultName.getCipherText());
        // 加密身份证
        CipherResult cipherResultIdCard = cipherService.encrypt(idCard, aad);
        userData.setIdCardCipherId(cipherResultIdCard.getId());
        userData.setNameCipherText(cipherResultName.getCipherText());
        return userRepository.save(userData);
    }

    /**
     * 解密
     */
    @RequestMapping(value = "/read")
    public void read(@RequestParam(value = "aad", required = false) String aad) throws Exception {
        UserData userData = userRepository.findById(1L).get();
        log.info("name : {}, idCard: {}", cipherService.decrypt(userData.getNameCipherId(), userData.getNameCipherText(), aad),
                cipherService.decrypt(userData.getIdCardCipherId(), userData.getIdCardCipherText(), aad));
    }

    /**
     * 脱敏姓名
     */
    private static String chineseName(String chineseName) {
        String name = StringUtils.left(chineseName, 1);
        return StringUtils.rightPad(name, StringUtils.length(chineseName), "*");
    }

    /**
     * 脱敏身份证
     */
    private static String idCard(String idCard) {
        String num = StringUtils.right(idCard, 4);
        return StringUtils.leftPad(num, StringUtils.length(idCard), "*");
    }

    public static void main(String[] args) {
        System.out.println(chineseName("张哈哈哈"));
        System.out.println(idCard("111223311111"));
    }
}
