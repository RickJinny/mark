package com.rickjinny.mark.controller.p12_exception.t03_PredefinedException;

public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
