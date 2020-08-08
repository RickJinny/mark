package com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata.bean;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserData {
    @Id
    private Long id;
    // 脱敏的身份证
    private String idCard;
    // 身份证加密id
    private Long idCardCipherId;
    // 身份证密文
    private String idCardCipherText;
    // 脱敏的姓名
    private String name;
    // 姓名加密id
    private Long nameCipherId;
    // 姓名加密
    private String nameCipherText;
}
