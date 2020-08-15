package com.rickjinny.mark.controller.p11_null.t01_AvoidNullPointerException;

import lombok.Getter;

public class FooService {
    @Getter
    private BarService barService;
}
