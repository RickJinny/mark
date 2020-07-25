package com.rickjinny.mark.controller.p17_oom.t01_UserNameAutoComplete;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public UserEntity() {

    }

    public UserEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
