package com.rickjinny.mark.controller.p22_apidesign.apiresponse;

import lombok.Data;

@Data
public class APIResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;
}
