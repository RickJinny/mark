package com.rickjinny.mark.controller.p15_serialization.t03_DeserializationConstructor;

import lombok.Data;

@Data
public class APIResultWrong {

    private boolean success;
    private int code;

    public APIResultWrong(){

    }

    public APIResultWrong(int code) {
        this.code = code;
        if (code == 2000) {
            success = true;
        } else {
            success = false;
        }
    }
}
