package com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class CipherData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 初始化向量
    private String iv;

    // 密钥
    private String secureKey;
}
