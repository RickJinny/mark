package com.rickjinny.mark.controller.p29_dataandcode.t03_xss;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户类
 */
@Entity
@Data
public class User {
    @Id
    private Long id;
    private String name;
}
