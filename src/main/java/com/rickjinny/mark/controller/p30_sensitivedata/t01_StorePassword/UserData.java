package com.rickjinny.mark.controller.p30_sensitivedata.t01_StorePassword;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserData {

    @Id
    private Long id;

    private String name;

    private String salt;

    private String password;
}
