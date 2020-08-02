package com.rickjinny.mark.controller.p27_clientdata.t04_TrustClientUserid;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface LongRequired {
    String sessionKey() default "currentUser";
}
