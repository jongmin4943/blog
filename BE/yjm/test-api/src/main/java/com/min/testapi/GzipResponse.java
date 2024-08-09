package com.min.testapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.zip.Deflater;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GzipResponse {

    // 레벨 설정 1~9 까지 설정이 가능하다
    int level() default Deflater.BEST_SPEED;

}
