package com.beyond.cloud.framework.lock.annotation;


import java.lang.annotation.*;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Lock {

    // SPEL 表达式
    String spel() ;

    // 失效时间 单位秒
    int expire() default 10;

    // log信息
    String logInfo() default "";
}
