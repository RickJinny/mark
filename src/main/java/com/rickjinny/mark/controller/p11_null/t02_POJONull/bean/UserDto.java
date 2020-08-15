package com.rickjinny.mark.controller.p11_null.t02_POJONull.bean;

import lombok.Data;

import java.util.Optional;

@Data
public class UserDto {
    private Long id;
    private Optional<String> name;
    private Optional<Integer> age;
}
