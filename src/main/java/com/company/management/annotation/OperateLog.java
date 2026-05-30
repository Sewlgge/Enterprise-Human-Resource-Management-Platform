package com.company.management.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解。标注在 Controller 方法上，由 AOP 切面写入 sys_log。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    /**
     * 操作描述，对应 sys_log.operation
     */
    String value();

    /**
     * 是否记录请求参数
     */
    boolean recordParams() default true;
}
