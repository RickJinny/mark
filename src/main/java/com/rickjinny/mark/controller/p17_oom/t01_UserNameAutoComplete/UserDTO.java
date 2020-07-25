package com.rickjinny.mark.controller.p17_oom.t01_UserNameAutoComplete;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class UserDTO {

    private String name;

    @EqualsAndHashCode.Exclude
    private String payload;

    /**
     * 对于每一个用户对象 UserDTO, 除了用户名，我们还加入了 10K 左右的数据模拟其用户信息。
     */
    public UserDTO(String name) {
        this.name = name;
        this.payload = IntStream.rangeClosed(1, 10_000)
                .mapToObj(__ -> "a")
                .collect(Collectors.joining(""));
    }

}
