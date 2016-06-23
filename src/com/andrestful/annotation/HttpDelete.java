package com.andrestful.annotation;

import com.andrestful.api.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HTTP-DELETE
 * Created by SCWANG on 2016/6/12.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpDelete {
    String value();
    HttpMethod method() default HttpMethod.DELETE;
}
