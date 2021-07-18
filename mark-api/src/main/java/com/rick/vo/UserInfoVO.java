package com.rick.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVO implements Serializable {

    private Long id;

    private String name;

    private Integer age;

}
