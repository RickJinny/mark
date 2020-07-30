package com.rickjinny.mark.controller.p19_spring_01.t02_AopMetrics;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public UserEntity() {

    }

    public UserEntity(String name) {
        this.name = name;
    }
}
