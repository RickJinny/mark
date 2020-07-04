package com.rickjinny.mark.controller.p22_apidesign.apiresponse;

import lombok.Getter;

public class APIException extends RuntimeException {

    @Getter
    private int errorCode;

    @Getter
    private String errorMessage;

    public APIException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public APIException(Throwable throwable, int errorCode, String errorMessage) {
        super(errorMessage, throwable);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
